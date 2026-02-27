import jwt from "jsonwebtoken";
import dotenv from "dotenv";

dotenv.config();

const verificarToken = (req, res, next) => {
    try {
        const token = req.header("Authorization");
        if (!token) {
            return res.status(400).json({ message: "Acceso denegado. Token no proporcionado." });
        }
        const extractedToken = token.split(' ')[1];
        const decoded = jwt.verify(extractedToken, process.env.SECRET_KEY);
        req.user = decoded.id_usuario;
        next();
    }
    catch (error) {
        res.status(400).json({ message: "Token inválido." });
    }
}

export { verificarToken };