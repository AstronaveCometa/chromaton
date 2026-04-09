import cors from "cors";
import express from "express";
import dotenv from "dotenv";
import userRoutes from "./src/routes/userRoutes.js";
import gamesRoutes from "./src/routes/gamesRoutes.js";

dotenv.config();
const app = express();
const puerto = process.env.PORT;
const host = process.env.HOST;
app.use(cors());
app.use(express.json());

app.use(userRoutes);
app.use(gamesRoutes);

app.get("/", (req, res) => {
  res.json({ message: "API funcionando 🚀" });
});

app.listen(puerto, console.log(`Servidor corriendo en http://${host}:${puerto}`));