package com.anhanguera.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button buttonAdd, buttonSub, buttonSendOrder;

    private TextView mQtd, mResumeOfOrder, mName, mResume;

    private int quantity = 0;

    private CheckBox cbBacon, cbCheese, cbOnion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuração dos botões para adicionar ou subtrair a quantidade
        add();
        sub();

        // Atualizar quantidade
        attQtd();

        // Configurando os métodos para enviar o pedido
        sendOrder();
    }

    private void add() {
        this.buttonAdd = findViewById(R.id.ButtonMais);

        this.buttonAdd.setOnClickListener(listener -> {
            this.quantity++;
            attQtd();
        });
    }

    private void sub() {
        this.buttonSub = findViewById(R.id.ButtonMenos);

        this.buttonSub.setOnClickListener(listener -> {
            if (quantity > 0) {
                quantity--;
                attQtd();
            }
        });
    }

    private void attQtd() {
        this.mQtd = findViewById(R.id.Qntd);
        this.mQtd.setText(String.valueOf(quantity));
    }

    private void sendOrder() {
        this.buttonSendOrder = findViewById(R.id.ButtonEnviar);

        this.buttonSendOrder.setOnClickListener(listener -> configOrder());
    }

    private void configOrder() {
        this.mName = findViewById(R.id.EditName);
        this.mResume = findViewById(R.id.Resumo);
        this.mResumeOfOrder = findViewById(R.id.ResumoPedido);
        this.cbBacon = findViewById(R.id.BoxBacon);
        this.cbCheese = findViewById(R.id.BoxCheese);
        this.cbOnion = findViewById(R.id.BoxOnionRings);

        String name = this.mName.getText().toString().trim();

        // Setando os Checkbox da view
        boolean bacon = cbBacon.isChecked();
        boolean cheese = cbCheese.isChecked();
        boolean onion = cbOnion.isChecked();

        double price = calculate(bacon, cheese, onion, quantity);

        String resume = "Nome do cliente: " + (name.isEmpty() ? "Não informado" : name) + "\n"
                + "Tem Bacon? " + (bacon ? "Sim" : "Não") + "\n"
                + "Tem Queijo? " + (cheese ? "Sim" : "Não") + "\n"
                + "Tem Onion Rings? " + (onion ? "Sim" : "Não") + "\n"
                + "Quantidade: " + quantity + "\n"
                + String.format("Preço final: R$ %.2f", price);

        this.mResumeOfOrder.setText(String.format("R$ %.2f", price));
        this.mResume.setText(resume);

        // Enviar email para o cliente
        sendEmail(name, resume);
    }

    private double calculate(boolean bacon, boolean cheese, boolean onion, int quantity) {
        double priceBase = 20;
        double additional = 0;

        if (bacon) additional += 2;
        if (cheese) additional += 2;
        if (onion) additional += 3;

        return (priceBase + additional) * quantity;
    }

    private void sendEmail(String name, String resume) {
        String email = "alunoaly@gmail.com";
        String subject = "Parabéns! " + name + " 🎉 Pedido criado com sucesso!";

        var uri = Uri.parse("mailto:" + email +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(resume));

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(uri);

        startActivity(intent);
    }
}