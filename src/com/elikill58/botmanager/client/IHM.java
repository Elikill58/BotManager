package com.elikill58.botmanager.client;

import com.elikill58.botmanager.common.Message;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class IHM extends Application {

	public static final int WARP_LENGHT = 220;
	private static Client client;
	public static FlowPane messagesPane, onlineUsers;
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private FlowPane contextPane;
	private TextField textInput, userNameInput;
	private Button stateButton;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chat");

		createContextPane();
		
		initRootLayout();
	}
	
	@Override
	public void stop() throws Exception {
		client.disconnectServer();
		System.exit(0);
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		GridPane grid = new GridPane();

		grid.add(messagesPane, 2, 0);
		grid.add(contextPane, 4, 0);
		grid.add(onlineUsers, 6, 0);

		// Create a BorderPane with a Text node in each of the five regions
		rootLayout = new BorderPane(grid);
		// Set the Size of the VBox
		rootLayout.setPrefSize(800, 800);
		// Set the Style-properties of the BorderPane
		//rootLayout.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;" + "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: blue;");

		// Show the scene containing the root layout.
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void createContextPane() {
		contextPane = new FlowPane();
		contextPane.setPadding(new Insets(5, 5, 0, 0));
		contextPane.setVgap(4);
		contextPane.setHgap(4);
		contextPane.setPrefWrapLength(170); // preferred width allows for two columns
		contextPane.setStyle("-fx-background-color: DAE6F3;");
		contextPane.setAlignment(Pos.BASELINE_CENTER);

		userNameInput = new TextField("Nom d'utilisateur");
		userNameInput.setPrefWidth(170);
		textInput = new TextField("Mon message");
		textInput.setPrefWidth(170);
		stateButton = new Button("Envoyer");
		stateButton.setPrefWidth(170);
		stateButton.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			@Override
			public void handle(MouseEvent e) {
				try {
					Message mess = new Message(-1, textInput.getText(), userNameInput.getText());
					client.getOut().writeObject(mess);
					client.getOut().flush();
					textInput.setText("");
					update("[Moi] > " + mess.toShow());
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		
		contextPane.getChildren().addAll(stateButton, userNameInput, textInput);
		
		messagesPane = new FlowPane();
		messagesPane.setPadding(new Insets(5, 5, 0, 0));
		messagesPane.setVgap(4);
		messagesPane.setHgap(4);
		messagesPane.setPrefWrapLength(300); // preferred width allows for two columns
		messagesPane.setStyle("-fx-background-color: DAE6F3;");
		messagesPane.setAlignment(Pos.BASELINE_CENTER);
		
		onlineUsers = new FlowPane();
		onlineUsers.setPadding(new Insets(5, 5, 0, 0));
		onlineUsers.setVgap(4);
		onlineUsers.setHgap(4);
		onlineUsers.setPrefWrapLength(170); // preferred width allows for two columns
		onlineUsers.setStyle("-fx-background-color: DAE6F3;");
		onlineUsers.setAlignment(Pos.BASELINE_CENTER);
		
		update("Server > Connection effectu� !");
	}

	public static void update(String msg) {
		if(msg == null)
			return;
		ObservableList<Node> child = messagesPane.getChildren();
		if(child.size() >= 10)
			child.remove(0);
		Label text = new Label(msg);
		text.setPrefWidth(300);
		child.add(text);

		ObservableList<Node> childUsers = onlineUsers.getChildren();
		childUsers.clear();
		Label onlineUserInformationtext = new Label("Utilisateur(s) connect� :");
		onlineUserInformationtext.setPrefWidth(170);
		childUsers.add(onlineUserInformationtext);
		client.getOnlineUsers().forEach((id, name) -> {
			Label userText = new Label(name);
			userText.setPrefWidth(170);
			childUsers.add(userText);
		});
	}
	
	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void run(Client client, String[] args) {
		IHM.client = client;
		launch(args);
	}

}
