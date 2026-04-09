import admin from "firebase-admin";

const verificarFirebaseToken = async (req, res, next) => {
    const headerToken = req.headers.authorization;

    if (!headerToken || !headerToken.startsWith('Bearer ')) {
        return res.status(401).send({ message: "No autorizado" });
    }

    const idToken = headerToken.split(' ')[1];

    try {
        const decodedToken = await admin.auth().verifyIdToken(idToken);
        
        req.user_uid = decodedToken.uid; 
        next();
    } catch (error) {
        console.error("Error al verificar token de Firebase:", error);
        res.status(403).send({ message: "Token inválido o expirado" });
    }
};

export { verificarFirebaseToken };