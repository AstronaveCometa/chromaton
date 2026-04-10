import admin from "firebase-admin";

export const verificarFirebaseToken = async (req, res, next) => {
    const token = req.headers.authorization?.split(" ")[1];
    if (!token) return res.status(401).json({ error: "No token provided" });

    try {
        const decodedToken = await admin.auth().verifyIdToken(token);
        // Guardamos el UID verificado en el request para que el controlador lo use
        req.user_uid = decodedToken.uid; 
        next();
    } catch (error) {
        res.status(401).json({ error: "Invalid token" });
    }
};