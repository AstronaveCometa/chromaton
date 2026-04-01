import { createPlayer } from "../models/playersModel";

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