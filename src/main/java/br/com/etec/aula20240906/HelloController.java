/*

package br.com.etec.aula20240906;

import br.com.etec.aula20240906.model.dao.ClienteDao;
import br.com.etec.aula20240906.model.database.Database;
import br.com.etec.aula20240906.model.database.DatabaseFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    // Atributos para manipulação de Banco de Dados
    // Classes do tipo Static não precisa ser instanciada
    // Classes do tipo Final outra não pode herdar dela, não pode ter classes filhas
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDao clienteDao = new ClienteDao();



    @FXML
    private Button btnCadastrar;

    @FXML
    private CheckBox chkCasado;

    @FXML
    private ToggleGroup grpSexo;

    @FXML
    private RadioButton rbFeminino;

    @FXML
    private RadioButton rbMasculino;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtNome;

    @FXML
    private TextArea txtAreaDados;

    @FXML
    private TextField txtBuscar;

    private Cliente cliente;
    private List<Cliente> listaClientes = new ArrayList<>(); // uma lista de Cliente chamada listaClientes



    @FXML
    protected void onClickCadastrar() { // cria metodo onClickCadastrar
        if(txtNome.getText().equals("")) {
            campoVazio("Nome em branco");
            txtNome.requestFocus();
        } else if (txtEmail.getText().equals("")) {
            campoVazio("Email em branco");
            txtEmail.requestFocus();
        } else if(txtTelefone.getText().equals("")) {
            campoVazio("Telefone em branco");
            txtTelefone.requestFocus();
        } else {
            String sexo = rbMasculino.isSelected()? "Masculino" : "Feminino"; // se rbMasculino estiver selecionado, masculino, se nao feminino
            int id = listaClientes.size() + 1; // tamanho do array listaClientes para id
            cliente = new Cliente(id, txtNome.getText(), txtEmail.getText(), txtTelefone.getText(), sexo, chkCasado.isSelected()); // pega os dados
            listaClientes.add(cliente);
            txtAreaDados.setText(listaClientes.toString());

            clienteDao.setConnection(connection);


            if(clienteDao.inserir(cliente)) {
                avisoBd("Salvar registro", "Cadastro de cliente", "Salvo com sucesso");
            } else {
                avisoBd("Salvar registro", "Cadastro de cliente", "Erro ao salvar");
            }

            limparCampos();
        }

    }









    @FXML
    private void avisoBd(String title, String header, String content) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(title);
        alerta.setHeaderText(header);
        alerta.setContentText(content);
        alerta.show();
        return;
    }

    @FXML
    private void limparCampos() {
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        rbMasculino.setSelected(false);
        rbFeminino.setSelected(false);
        chkCasado.setSelected(false);
        txtNome.requestFocus();
    }

    @FXML
    private void campoVazio(String msg) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("ERRO");
        alerta.setHeaderText("Campo em branco");
        alerta.setContentText(msg);
        alerta.show();
        return;
    }

    @FXML
    protected void onClickBuscar() {
        Integer idBusca;

        try {
            idBusca = Integer.parseInt(txtBuscar.getText());
        } catch (Exception err) {
            Alert alertErro = new Alert(Alert.AlertType.ERROR);
            alertErro.setTitle("Erro");
            alertErro.setHeaderText("Erro de conversão");
            alertErro.setContentText("O campo não é um número válido");
            alertErro.show();
            return;
        }

        for(Integer i = 0; i < listaClientes.size(); i++) {
            if(listaClientes.get(i).getId() == idBusca) {
                Cliente cliente1 = listaClientes.get(i);
                populaCampos(cliente1);
            } else if(listaClientes.size() < Integer.parseInt(txtBuscar.getText())) {
                Alert alertErro = new Alert(Alert.AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro de conversão");
                alertErro.setContentText("O campo não é um número válido");
                alertErro.show();
                return;
            }
        }
    }






    @FXML
    private void populaCampos(Cliente cli) {
        txtNome.setText(cli.getNome());
        txtEmail.setText(cli.getEmail());
        txtTelefone.setText(cli.getTelefone());

        if(cli.getSexo().equals("Feminino")) {
            rbFeminino.setSelected(true);
            rbMasculino.setSelected(false);
        } else {
            rbMasculino.setSelected(true);
            rbFeminino.setSelected(false);
        }

        if(cli.getCasado()) {
            chkCasado.setSelected(true);
        } else {
            chkCasado.setSelected(false);
        }
    }


    @FXML
    protected void onClickBuscarBanco() {
        int idBusca;

        try {
            idBusca = Integer.parseInt(txtBuscar.getText());

        } catch (Exception ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("ERRO");
            alerta.setHeaderText("Erro de conversão");
            alerta.setContentText("O campo não é um número válido");
            alerta.show();
            return;
        }

        clienteDao.setConnection(connection);
        cliente = clienteDao.getClienteById(idBusca);

        if(cliente.getId() != null) {
            populaCampos(cliente);
            txtNome.setEditable(false);
            txtEmail.setEditable(false);
            txtTelefone.setEditable(false);

        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Busca de cliente");
            alerta.setHeaderText("Busca de cliente");
            alerta.setContentText("Não existe um cliente com esse id");
            alerta.show();
            return;
        }
    }

    @FXML
    protected void onClickDeletar() {
        int idBusca;

        try {
            idBusca = Integer.parseInt(txtBuscar.getText());

        } catch (Exception ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("ERRO");
            alerta.setHeaderText("Erro de conversão");
            alerta.setContentText("O campo não é um número válido");
            alerta.show();
            return;
        }

        clienteDao.setConnection(connection);

        if(clienteDao.deleteClienteById(idBusca)) {
            avisoBd("Salvar cliente", "Cadastro deletado", "Deletado com sucesso");
        } else {
            avisoBd("Erro ao deletar", "Erro ao deletar", "Erro ao deletar");
        }
    }
}


*/


package br.com.etec.aula20240906;

import br.com.etec.aula20240906.model.dao.ClienteDao;
import br.com.etec.aula20240906.model.database.Database;
import br.com.etec.aula20240906.model.database.DatabaseFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Cliente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    // Atributos para manipulação de Banco de Dados
    // Classes do tipo Static não precisam ser instanciadas
    // Classes do tipo Final não podem ser herdadas, não podem ter classes filhas
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDao clienteDao = new ClienteDao();

    @FXML
    private Button btnCadastrar;
    @FXML
    private CheckBox chkCasado;
    @FXML
    private ToggleGroup grpSexo;
    @FXML
    private RadioButton rbFeminino;
    @FXML
    private RadioButton rbMasculino;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtNome;
    @FXML
    private TextArea txtAreaDados;
    @FXML
    private TextField txtBuscar;

    private Cliente cliente;
    private List<Cliente> listaClientes = new ArrayList<>(); // uma lista de Cliente chamada listaClientes

    @FXML
    protected void onClickCadastrar() { // cria metodo onClickCadastrar
        if (txtNome.getText().equals("")) {
            campoVazio("Nome em branco");
            txtNome.requestFocus();
        } else if (txtEmail.getText().equals("")) {
            campoVazio("Email em branco");
            txtEmail.requestFocus();
        } else if (txtTelefone.getText().equals("")) {
            campoVazio("Telefone em branco");
            txtTelefone.requestFocus();
        } else {
            // Verifica qual radio button está selecionado para definir o sexo
            String sexo = rbMasculino.isSelected() ? "Masculino" : "Feminino";

            // Define a conexão para o clienteDao
            clienteDao.setConnection(connection);

            // Cria um novo cliente com os dados dos campos de texto
            cliente = new Cliente(0, txtNome.getText(), txtEmail.getText(), txtTelefone.getText(), sexo, chkCasado.isSelected());

            // Insere o cliente no banco de dados
            if (clienteDao.inserir(cliente)) {
                avisoBd("Salvar registro", "Cadastro de cliente", "Salvo com sucesso");

                // Atualiza o TextArea com todos os clientes do banco de dados
                atualizarTextArea();
            } else {
                avisoBd("Salvar registro", "Cadastro de cliente", "Erro ao salvar");
            }

            // Limpa os campos após a inserção
            limparCampos();
        }
    }

    @FXML
    private void avisoBd(String title, String header, String content) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(title);
        alerta.setHeaderText(header);
        alerta.setContentText(content);
        alerta.show();
    }

    @FXML
    private void limparCampos() {
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        rbMasculino.setSelected(false);
        rbFeminino.setSelected(false);
        chkCasado.setSelected(false);
        txtNome.requestFocus();
    }

    @FXML
    private void campoVazio(String msg) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("ERRO");
        alerta.setHeaderText("Campo em branco");
        alerta.setContentText(msg);
        alerta.show();
    }

    @FXML
    protected void onClickBuscar() {
        Integer idBusca;
        try {
            idBusca = Integer.parseInt(txtBuscar.getText());
        } catch (Exception err) {
            Alert alertErro = new Alert(Alert.AlertType.ERROR);
            alertErro.setTitle("Erro");
            alertErro.setHeaderText("Erro de conversão");
            alertErro.setContentText("O campo não é um número válido");
            alertErro.show();
            return;
        }

        for (Integer i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getId() == idBusca) {
                Cliente cliente1 = listaClientes.get(i);
                populaCampos(cliente1);
            } else if (listaClientes.size() < Integer.parseInt(txtBuscar.getText())) {
                Alert alertErro = new Alert(Alert.AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro de conversão");
                alertErro.setContentText("O campo não é um número válido");
                alertErro.show();
                return;
            }
        }
    }

    @FXML
    private void populaCampos(Cliente cli) {
        txtNome.setText(cli.getNome());
        txtEmail.setText(cli.getEmail());
        txtTelefone.setText(cli.getTelefone());
        if (cli.getSexo().equals("Feminino")) {
            rbFeminino.setSelected(true);
            rbMasculino.setSelected(false);
        } else {
            rbMasculino.setSelected(true);
            rbFeminino.setSelected(false);
        }
        chkCasado.setSelected(cli.getCasado());
    }

    @FXML
    protected void onClickBuscarBanco() {
        int idBusca;
        try {
            idBusca = Integer.parseInt(txtBuscar.getText());
        } catch (Exception ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("ERRO");
            alerta.setHeaderText("Erro de conversão");
            alerta.setContentText("O campo não é um número válido");
            alerta.show();
            return;
        }

        clienteDao.setConnection(connection);
        cliente = clienteDao.getClienteById(idBusca);

        if (cliente.getId() != null) {
            populaCampos(cliente);
            txtNome.setEditable(false);
            txtEmail.setEditable(false);
            txtTelefone.setEditable(false);
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Busca de cliente");
            alerta.setHeaderText("Busca de cliente");
            alerta.setContentText("Não existe um cliente com esse id");
            alerta.show();
        }
    }

    @FXML
    protected void onClickDeletar() {
        int idBusca;
        try {
            idBusca = Integer.parseInt(txtBuscar.getText());
        } catch (Exception ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("ERRO");
            alerta.setHeaderText("Erro de conversão");
            alerta.setContentText("O campo não é um número válido");
            alerta.show();
            return;
        }

        clienteDao.setConnection(connection);

        if (clienteDao.deleteClienteById(idBusca)) {
            avisoBd("Salvar cliente", "Cadastro deletado", "Deletado com sucesso");

            // Atualiza o TextArea com todos os clientes do banco de dados após a exclusão
            atualizarTextArea();
        } else {
            avisoBd("Erro ao deletar", "Erro ao deletar", "Erro ao deletar");
        }
    }

    /**
     * Atualiza o TextArea com todos os clientes presentes no banco de dados.
     */
    private void atualizarTextArea() {
        try {
            // Define a conexão para o clienteDao
            clienteDao.setConnection(connection);

            // Recupera todos os clientes do banco de dados
            ResultSet resultSet = clienteDao.listarTodos();

            // StringBuilder para construir o texto a ser exibido no TextArea
            StringBuilder sb = new StringBuilder();

            // Itera sobre os resultados da consulta
            while (resultSet.next()) {
                // Obtém os dados de cada registro
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String email = resultSet.getString("email");
                String telefone = resultSet.getString("telefone");
                String sexo = resultSet.getString("sexo");
                boolean casado = resultSet.getBoolean("casado");

                // Adiciona os dados ao StringBuilder
                sb.append("ID: ").append(id)
                        .append(", Nome: ").append(nome)
                        .append(", Email: ").append(email)
                        .append(", Telefone: ").append(telefone)
                        .append(", Sexo: ").append(sexo)
                        .append(", Casado: ").append(casado)
                        .append("\n");
            }

            // Define o texto construído no TextArea
            txtAreaDados.setText(sb.toString());

        } catch (SQLException ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("ERRO");
            alerta.setHeaderText("Erro ao atualizar TextArea");
            alerta.setContentText(ex.getMessage());
            alerta.show();
        }
    }
}