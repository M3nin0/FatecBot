package controller;

import com.pengrad.telegrambot.model.Update;

import model.Model;
import view.View;

public class AuthController implements ProcessController {

	private Model model;
	private View view;

	private String name = null;
	private String password = null;

	public AuthController(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	public void process(Update update) {
		view.sendTypingMessage(update);

		if (view.getState() == View.IS_RECOVERY_USER) {
			String[] userInfo = model.recoveryUser(update.message().chat().id(), true);

			// Verificando se o usuário está registrado
			if (userInfo != null) {
				name = userInfo[0];
				password = userInfo[1];
			}
			// Retorna o estado original do bot.
			view.setState(View.IS_NOTHING);

		} else if (view.getState() == View.IS_REGISTERING_USERNAME) {
			name = update.message().text();
		} else if (view.getState() == View.IS_REGISTERING_PASSWORD) {
			password = update.message().text();
		}

		if (name != null && password != null) {
			model.addUser(update.message().chat().id(), name, password);
			view.setState(View.IS_NOTHING);
		}
	}
}
