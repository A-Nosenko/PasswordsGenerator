package go.pass.controller;

import go.pass.Generator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

/**
 * @author Anatolii Nosenko
 * @version 1.0 6/19/2019.
 */
public class MainController implements Initializable {
    @FXML
    private Text textTotalCount;

    @FXML
    private Text textAlreadyGeneratedCount;

    @FXML
    private Text textRemainderCount;

    @FXML
    private TextField inputCountTextField;

    @FXML
    private Text actionTarget;

    @FXML
    private Button startButton;

    @FXML
    public void startGeneration() {
        new Thread(() -> {
            long start = System.nanoTime();
            try {
                toGenerate = Integer.parseInt(inputCountTextField.getText().trim());
            } catch (Exception e) {
                actionTarget.setText("Ошибка! Некорректно задано количество паролей!");
                return;
            }

            if (toGenerate <= 0) {
                actionTarget.setText("Ошибка! Количество паролей должно быть от 1 до 10.000.000");
                return;
            }

            if (toGenerate > 10_000_000) {
                actionTarget.setText("Ошибка! Количество паролей должно быть не боле 10.000.000");
                return;
            }

            startButton.setDisable(true);
            actionTarget.setText("Генерация паролей в процессе. Количество = " + toGenerate + " Подождите...");
            try {
                generator.generate(toGenerate);
            } catch (Exception e) {
                actionTarget.setText("Ошибка генерации паролей! " + e.getMessage());
                return;
            } finally {
                initReady();
                initRemainder();
                startButton.setDisable(false);
                init();
            }
            double finish = (System.nanoTime() - start) / 1000000000d;
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            actionTarget.setText("Генерация паролей завершена. Количество = " + toGenerate +
                    "\nЗатрачено времени: " + decimalFormat.format(finish) + "c");
        }).start();
    }

    private Generator generator = Generator.getGenerator();
    private int toGenerate;

    private void initTotal() {
        textTotalCount.setText(generator.getTotalPass().toString());
    }

    private void initReady() {
        textAlreadyGeneratedCount.setText(generator.getReady().toString());
    }

    private void initRemainder() {
        textRemainderCount.setText(generator.getTotalPass().subtract(generator.getReady()).toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        initTotal();
        initReady();
        initRemainder();
    }
}
