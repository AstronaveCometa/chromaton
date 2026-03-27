import { Router } from "express";
import { registerUser, getUserByIdController } from "../controllers/usersController.js";

const router = Router();

router.post("/usuarios", registerUser);
router.get("/usuarios/:user_id", getUserByIdController);


export default router;