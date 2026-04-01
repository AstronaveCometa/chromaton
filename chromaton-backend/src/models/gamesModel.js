import pool from '../../db/config.js';
import { createPlayer } from './playersModel.js';

export const createGame = async (datos) => {
    const { host_id, game_password } = datos;

    const query = `
        INSERT INTO games (host_id, game_password, status, current_turn_player_id, round_count) VALUES ($1, $2, $3, $4, $5) RETURNING *`;
    const values = [host_id, game_password, 'waiting', null, 0];
    try {
        const res = await pool.query(query, values);
        return res.rows[0];
    } catch (err) {
        console.error('Error al crear el juego 😱:', err);
        throw err;
    }
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
    // Verificar el estado del juego antes de permitir unirse
    checkGameStatus(game_id).then(status => {
        if (status !== 'waiting') {
            throw new Error('No se puede unir al juego, el juego ya ha comenzado o ha terminado.');
        }
    }).catch(err => {
        console.error('Error al verificar el estado del juego:', err);
        throw err;
    });

    // Verificar la contraseña del juego antes de permitir unirse
    checkGamePassword(game_id, game_password).then(isValid => {
        if (!isValid) {
            throw new Error('Contraseña del juego incorrecta.');
        }
    }).catch(err => {
        console.error('Error al verificar la contraseña del juego:', err);
        throw err;
    });

    //Incrementar el número de jugadores en el juego
    const query = `UPDATE games SET players_counter = players_counter + 1 WHERE game_id = $1 RETURNING *`;
    try {
        const res = await pool.query(query, [game_id]);

        //Crear un nuevo jugador en la tabla players
        const player = await createPlayer({game_id, user_id});
        console.log('Jugador creado:', player);
        return res.rows[0];
    } catch (err) {
        console.error('Error al incrementar número de jugadores:', err);
        throw err;
    }
};

export const changeGameStatus = async (game_id, new_status) => {
    const query = `UPDATE games SET status = $1 WHERE game_id = $2 RETURNING *`;
    try {
        const res = await pool.query(query, [new_status, game_id]);
        return res.rows[0];
    } catch (err) {
        console.error('Error al cambiar el estado del juego:', err);
        throw err;
    }
};