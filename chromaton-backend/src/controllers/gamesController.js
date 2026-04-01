import { createGame, getGameById, joinGame, changeGameStatus } from '../models/gamesModel.js';

export const createGameController = async (req, res) => {
    const { host_id, game_password } = req.body;
    if (!host_id) {
        return res.status(400).json({ error: 'host_id es requerido' });
    }
    if (!game_password) {
        return res.status(400).json({ error: 'game_password es requerido' });
    }
    try {
        const game = await createGame({ host_id, game_password });
        res.status(201).json(game);
    } catch (err) {
        res.status(500).json({ error: 'Error al crear el juego 😱' });
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
    const { game_id, user_id, game_password } = req.body;
    if (!game_id) {
        return res.status(400).json({ error: 'game_id es requerido' });
    }
    if (!user_id) {
        return res.status(400).json({ error: 'user_id es requerido' });
    }
    if (!game_password) {
        return res.status(400).json({ error: 'game_password es requerido' });
    }
    try {
        const game = await joinGame(game_id, user_id, game_password);
        res.json(game);
    } catch (err) {
        res.status(500).json({ error: 'Error al unirse al juego 😱' });
    }
};

export const startGameController = async (req, res) => {
    const { game_id } = req.body;
    if (!game_id) {
        return res.status(400).json({ error: 'game_id es requerido' });
    }
    try {
        const game = await changeGameStatus(game_id, 'playing');
        res.json(game);
    } catch (err) {
        res.status(500).json({ error: 'Error al cambiar el estado del juego' });
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
