import pool from '../../db/config.js';
import { createPlayer, getPlayersByGameId } from './playersModel.js';

export const createGame = async (datos) => {
    const { host_id, game_password } = datos;
    const query = `
        INSERT INTO games (host_id, game_password, status, current_turn_player_id, round_count) 
        VALUES ($1, $2, $3, $4, $5) RETURNING *`;
    const values = [host_id, game_password, 'waiting', null, 0];

    const res = await pool.query(query, values);
    const game = res.rows[0];

    // El host también es el primer jugador
    await createPlayer({ game_id: game.game_id, user_id: host_id });

    return game;
};

export const getGameById = async (game_id) => {
    const query = `SELECT * FROM games WHERE game_id = $1`;
    try {
        const res = await pool.query(query, [game_id]);
        return res.rows[0];
    }
    catch (err) {
        console.error('Error al obtener el juego 😱:', err);
        throw err;
    }
};

export const checkGameStatus = async (game_id) => {
    const query = `SELECT status FROM games WHERE game_id = $1`;
    try {
        const res = await pool.query(query, [game_id]);
        return res.rows[0].status;
    } catch (err) {
        console.error('Error al verificar el estado del juego:', err);
        throw err;
    }
};

export const checkGamePassword = async (game_id, game_password) => {
    const query = `SELECT game_password FROM games WHERE game_id = $1`;
    try {
        const res = await pool.query(query, [game_id]);
        return res.rows[0].game_password === game_password;
    } catch (err) {
        console.error('Error al verificar la contraseña del juego:', err);
        throw err;
    }
};

export const joinGame = async (game_id, user_id, game_password) => {
    // 1. Obtener datos del juego
    const game = await getGameById(game_id);
    if (!game) throw new Error('El juego no existe.');

    // 2. Validaciones
    if (game.status !== 'waiting') throw new Error('El juego ya ha comenzado.');
    if (game.game_password !== game_password) throw new Error('Contraseña incorrecta.');

    const existingPlayers = await getPlayersByGameId(game_id);
    
    // Si el jugador NO está en la lista, lo agregamos
    if (!existingPlayers.some(player => player.user_id === user_id)) {
        await createPlayer({ game_id, user_id });
        const updateQuery = `UPDATE games SET players_counter = players_counter + 1 WHERE game_id = $1 RETURNING *`;
        const updateRes = await pool.query(updateQuery, [game_id]);
        return updateRes.rows[0]; // Retornamos el juego actualizado
    }

    // Si el jugador YA ESTABA (ej. se le cerró la app y volvió a entrar), retornamos el juego actual
    return game;
};

export const changeGameStatus = async (game_id, new_status) => {
    const query = `UPDATE games SET status = $1 WHERE game_id = $2 RETURNING *`;
    const res = await pool.query(query, [new_status, game_id]);
    return res.rows[0];
};

//isGameFinished(game_id)
//setWinner(player_id)