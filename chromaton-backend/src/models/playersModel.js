import pool from '../../db/config.js';

export const createPlayer = async (datos) => {
    const { game_id, user_id } = datos;

    const query = `
        INSERT INTO players (game_id, user_id) VALUES ($1, $2) RETURNING *`;
    const values = [game_id, user_id];
    try {
        const res = await pool.query(query, values);
        return res.rows[0];
    } catch (err) {
        console.error('Error al crear el jugador:', err);
        throw err;
    }
};