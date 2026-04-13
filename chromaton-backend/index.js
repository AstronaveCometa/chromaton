import cors from "cors";
import express from "express";
import dotenv from "dotenv";
import userRoutes from "./src/routes/userRoutes.js";
import gamesRoutes from "./src/routes/gamesRoutes.js";
import playersRoutes from "./src/routes/playersRoutes.js";
import admin from "firebase-admin";

admin.initializeApp({
  credential: admin.credential.cert({
    projectId: process.env.FIREBASE_PROJECT_ID,
    clientEmail: process.env.FIREBASE_CLIENT_EMAIL,
    privateKey: process.env.FIREBASE_PRIVATE_KEY?.replace(/\\n/g, '\n'),
  })
});


dotenv.config();
const app = express();
const puerto = process.env.PORT;
const host = process.env.HOST;
app.use(cors());
app.use(express.json());

app.use(userRoutes);
app.use(gamesRoutes);
app.use(playersRoutes);

app.get("/", (req, res) => {
  res.json({ message: "API funcionando 🚀" });
});

app.listen(puerto, console.log(`Servidor corriendo en http://${host}:${puerto}`));