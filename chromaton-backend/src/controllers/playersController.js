import { createPlayer, getPlayersByGameId } from "../models/playersModel.js";

export const createPlayerController = async (req, res) => {
    const { game_id, user_id } = req.body;
    if (!game_id) {
        return res.status(400).json({ error: 'game_id es requerido' });
    }
    if (!user_id) {
        return res.status(400).json({ error: 'user_id es requerido' });
    }
    try {
        const player = await createPlayer({ game_id, user_id });
        res.status(201).json(player);
    } catch (err) {
        res.status(500).json({ error: 'Error al crear el jugador 😱' });
    }
};

export const getPlayersByGameIdController = async (req, res) => {
    const { game_id } = req.params;
    if (!game_id) {
        return res.status(400).json({ error: 'game_id es requerido' });
    }
    try {
        const players = await getPlayersByGameId(game_id);
        res.status(200).json(players);
    } catch (err) {
        res.status(500).json({ error: 'Error al obtener los jugadores del juego 😱' });
    }
};