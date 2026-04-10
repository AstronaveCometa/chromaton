import { createUser, getUserById } from "../models/usersModel.js";

export const registerUser = async (req, res) => {
    try {
        const { name, email } = req.body; 
        const user_id = req.user_uid;

        const newUser = await createUser({ user_id, name, email });
        res.status(201).json(newUser);
    } catch (error) {
        console.error("Error en el registro:", error);
        res.status(500).json({ error: error.message });
    }
}

export const getUserByIdController = async (req, res) => {
    try {
        const user_id = req.params.user_id;
        const user = await getUserById(user_id);
        
        if (!user) {
            return res.status(404).json({ message: "Usuario no encontrado." });
        }
        res.status(200).json(user);
    } catch (error) {
        res.status(500).json({ error: "Error interno." });
    }
}