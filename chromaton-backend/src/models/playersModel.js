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

export const getPlayersByGameId = async (game_id) => {
    const query = `SELECT 
    p.player_id as player_id,
    u.name as name
    FROM players p LEFT JOIN users u ON p.user_id = u.user_id WHERE p.game_id = $1`;
    try {
        const res = await pool.query(query, [game_id]);
        return res.rows;
    } catch (err) {
        console.error('Error al obtener los jugadores del juego:', err);
        throw err;
    }
};

//getPlayerById(player_id)
//getTurnsByGameId(game_id)
//dealSecretDicesForPlayer(game_id, player_id)
//dealHandForPlayer(game_id, player_id)
//isPlayerDead(game_id, player_id)
//setPlayerDead(player_id)