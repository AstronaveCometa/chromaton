import pool from '../../db/config.js';

export const createUser = async (datos) => {
    const { user_id, name, email } = datos;
    const consultaSQL = {
        text: "INSERT INTO users (user_id, name, email) VALUES ($1, $2, $3) RETURNING user_id, name, email",
        values: [user_id, name, email]
    };
    const resultado = await pool.query(consultaSQL);
    return resultado.rows[0];
}

export const getUserByEmail = async (email) => {
    const consultaSQL = {
        text: "SELECT * FROM users WHERE email = $1",
        values: [email]
    };
    const resultado = await pool.query(consultaSQL);
    return resultado.rows[0];
}

export const getUserById = async (user_id) => {
    const consultaSQL = {
        text: "SELECT * FROM users WHERE user_id = $1",
        values: [user_id]
    };
    const resultado = await pool.query(consultaSQL);
    return resultado.rows[0];
}