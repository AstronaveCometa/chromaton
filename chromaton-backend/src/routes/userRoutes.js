import { Router } from "express";
import { registerUser, getUserByIdController } from "../controllers/usersController.js";
import { verificarFirebaseToken } from "../middleware/authMiddleware.js";

const router = Router();

router.post("/users", verificarFirebaseToken, registerUser);
router.get("/users/:user_id", verificarFirebaseToken, getUserByIdController);


export default router;