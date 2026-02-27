import { createUser, getUserById } from "../models/usersModel.js";

export const registerUser = async (req, res) => {
    try {
        const { nombre, email, password } = req.body;
        const newUser = await createUser({ nombre, email, password });
        res.status(201).json(newUser);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

export const getUserByIdController = async (req, res) => {
    try {
        const id_usuario = req.user; 
        const user = await getUserById(id_usuario);
        
        if (!user) {
            return res.status(404).json({ message: "Usuario no encontrado." });
        }
        res.status(200).json(user);
    } catch (error) {
        res.status(500).json({ error: "Error interno." });
    }
}