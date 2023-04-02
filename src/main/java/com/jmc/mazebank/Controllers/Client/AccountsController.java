package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    // right control
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_cv_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        trans_to_sv_btn.setOnAction(event -> sendToSaving());
        trans_to_cv_btn.setOnAction(event -> sendToChecking());

    }
    private void bindData(){
        ch_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        sv_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
        ch_acc_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
        sv_acc_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
        sv_acc_date.textProperty().bind(Model.getInstance().getClient().dateProperty().asString());
        ch_acc_date.textProperty().bind(Model.getInstance().getClient().dateProperty().asString());
    }

    private void sendToSaving() {
        double amount = Double.parseDouble(amount_to_sv.getText());
        String sender = Model.getInstance().getClient().pAddressProperty().get();
        Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "ADD");
        Model.getInstance().getDatabaseDriver().updateCheckBalance(sender, amount, "SUB");
        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(sender));
        Model.getInstance().getClient().checkingAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getCheckingsAccountBalance(sender));

        amount_to_sv.setText("");
    }
    private void sendToChecking() {
        double amount = Double.parseDouble(amount_to_ch.getText());
        String sender = Model.getInstance().getClient().pAddressProperty().get();
        Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "SUB");
        Model.getInstance().getDatabaseDriver().updateCheckBalance(sender, amount, "ADD");

        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(sender));
        Model.getInstance().getClient().checkingAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getCheckingsAccountBalance(sender));

        amount_to_ch.setText("");
    }
}
