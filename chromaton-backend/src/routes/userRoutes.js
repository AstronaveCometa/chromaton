import { Router } from "express";
import { registerUser, getUserByIdController } from "../controllers/usersController.js";
import { verificarToken } from "../middleware/verificarToken.js";

const router = Router();

router.post("/usuarios", registerUser);
router.get("/usuarios", verificarToken, getUserByIdController);


export default router;