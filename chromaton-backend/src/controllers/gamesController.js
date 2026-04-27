import { createGame, getGameById, joinGame, changeGameStatus, startGameAndGenerateDices } from '../models/gamesModel.js';

export const createGameController = async (req, res) => {
    const { game_password } = req.body;
    const host_id = req.user_uid; // Acá se obtiene la identidad protegida por Firebase Middleware

    if (!game_password) return res.status(400).json({ error: 'La contraseña es requerida' });

    try {
        const game = await createGame({ host_id, game_password });
        res.status(201).json(game);
    } catch (err) {
        res.status(500).json({ error: 'Error al crear el juego' });
    }
};

export const getGameByIdController = async (req, res) => {
    const { game_id } = req.params;
    console.log('Obteniendo juego con ID:', game_id);
    try {
        const game = await getGameById(game_id);
        if (!game) {
            return res.status(404).json({ error: 'Juego no encontrado' });
        }
        res.json(game);
    } catch (err) {
        res.status(500).json({ error: 'Error al obtener el juego 😱' });
    }
};

export const joinGameController = async (req, res) => {
    const { game_id, game_password } = req.body;
    const user_id = req.user_uid;

    try {
        const game = await joinGame(game_id, user_id, game_password);
        res.json(game);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
};

export const startGameController = async (req, res) => {
    const { game_id } = req.body;
    const requester_id = req.user_uid;

    try {
        const game = await getGameById(game_id);
        if (!game) return res.status(404).json({ error: 'Juego no encontrado' });
        
        if (game.host_id !== requester_id) {
            return res.status(403).json({ error: 'Solo el host inicia la partida' });
        }

        // Obtenemos cuántos jugadores hay para pasarle al generador
        const players = await getPlayersByGameId(game_id);
        
        // Llamamos a la función "atómica" que hace todo
        await startGameAndGenerateDices(game_id, players.length);
        
        res.json({ message: 'Partida iniciada y dados generados' });
    } catch (err) {
        res.status(500).json({ error: 'Fallo al iniciar partida' });
    }
};

export const endGameController = async (req, res) => {
    const { game_id } = req.body;
    if (!game_id) {
        return res.status(400).json({ error: 'game_id es requerido' });
    }
    try {
        const game = await changeGameStatus(game_id, 'finished');
        res.json(game);
    } catch (err) {
        res.status(500).json({ error: 'Error al cambiar el estado del juego' });
    }
};
