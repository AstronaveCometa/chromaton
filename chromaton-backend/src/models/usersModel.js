import bcrypt from "bcryptjs";
import pool from '../../db/config.js';

export const createUser = async (datos) => {
    const { name, email, password } = datos;
    const hashedPassword = await bcrypt.hash(password, 10);
    const consultaSQL = {
        text: "INSERT INTO users (name, email, password) VALUES ($1, $2, $3) RETURNING name, email",
        values: [name, email, hashedPassword]
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

export const getUserById = async (id_usuario) => {
    const consultaSQL = {
        text: "SELECT * FROM users WHERE user_id = $1",
        values: [id_usuario]
    };
    const resultado = await pool.query(consultaSQL);
    return resultado.rows[0];
}