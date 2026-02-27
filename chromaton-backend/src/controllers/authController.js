import { getUserByEmail } from "../models/usersModel.js";
import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";

const loginUser = async (req, res) => {
    try {
        const { email, password } = req.body;
        const user = await getUserByEmail(email);

        if (!user) {
            return res.status(404).json({ message: "El usuario o la password es incorrecta." });
        }
        const isPasswordValid = await bcrypt.compare(password, user.password);
        if (!isPasswordValid) {
            return res.status(404).json({ message: "El usuario o la password es incorrecta." });
        }
        const token = jwt.sign({ id_usuario: user.id_usuario}, process.env.SECRET_KEY, { expiresIn: '60m' });
        res.status(200).json({ token });

    } catch (error) {
        console.error(error);
        res.status(500).json({ error: "Error interno del servidor." });
    }
}

export { loginUser };