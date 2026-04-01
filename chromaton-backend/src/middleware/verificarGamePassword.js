import { getGameById } from "../models/gamesModel";

export const verificarGamePassword = async (req, res, next) => {
    const { game_id, game_password } = req.body;
    if (!game_id) {
        return res.status(400).json({ error: 'game_id es requerido' });
    }
    if (!game_password) {
        return res.status(400).json({ error: 'game_password es requerido' });
    }
    try {
        const game = await getGameById(game_id);
        if (!game) {
            return res.status(404).json({ error: 'Juego no encontrado' });
        }
        if (game.game_password !== game_password) {
            return res.status(401).json({ error: 'Contraseña incorrecta' });
        }
        next();
    } catch (err) {
        res.status(500).json({ error: 'Error al verificar la contraseña del juego 😱' });
    }
};
