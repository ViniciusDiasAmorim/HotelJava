package dao;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Reserva;

public class ReservaDAO {
	
	private Connection connection;
	
	public ReservaDAO() {
		
	}
	
	public ReservaDAO(Connection connection) {
		this.connection = connection;
	}
	
	public int inserirReserva(String dataEntrada,String dataSaida,String valor,String formaPagamento) {
		double valorConvertido = Double.parseDouble(valor);
		
		String querySql = "INSERT INTO reservas(DataEntrada,DataSaida,Valor,FormaPagamento) VALUES(?,?,?,?);";
		try(PreparedStatement prst = connection.prepareStatement(querySql,Statement.RETURN_GENERATED_KEYS)) {
			prst.setString(1, dataEntrada);
			prst.setString(2, dataSaida);
			prst.setDouble(3, valorConvertido);
			prst.setString(4, formaPagamento);
			prst.execute();
			
			try(ResultSet rst = prst.getGeneratedKeys()){
				while(rst.next()) {
					return rst.getInt(1);
				}
			}
			
		}
		catch(Exception e ) { 
			throw new RuntimeException(e);
		}
		return 0;
	}
	
	public double valor(String dataEntrada, String dataSaida)  {
		
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		LocalDate dataEntradaFormatada = LocalDate.parse(dataEntrada, formato);
		LocalDate dataSaidaFormatada = LocalDate.parse(dataSaida, formato);
		
		int dias = 0;
		
		while(!dataEntradaFormatada.equals(dataSaidaFormatada)) {
			dataEntradaFormatada = dataEntradaFormatada.plusDays(1);
			dias++;
		}
		
		double valor = dias * 20;
		
		return valor;
	}
	
	public List<Reserva> listaReservas() {
		
		List<Reserva> listaReserva = new ArrayList<Reserva>();
		String querySql = "SELECT * FROM RESERVAS;";
		
		try(PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.execute();
			
			try(ResultSet rst = prst.getResultSet()) {
				while(rst.next()) {
					int id = rst.getInt(1);
					String dataEntrada = rst.getString(2) ;
					String dataSaida = rst.getString(3);
					BigDecimal valor = rst.getBigDecimal(4);
					String formaPagamento = rst.getString(5);
					
					Reserva reserva = new Reserva(id,dataEntrada,dataSaida,valor,formaPagamento);
					
					listaReserva.add(reserva);
				}
				return listaReserva;
			}
			
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void editar(int id, String dataEntrada,String dataSaida,String formaPagamento) {
		ReservaDAO reserva = new ReservaDAO();
		String querySql = "UPDATE reservas R SET R.DataEntrada = ?, R.DataSaida = ?, R.Valor = ?, R.FormaPagamento = ? WHERE id = ?";
		
		try(PreparedStatement prst = connection.prepareStatement(querySql)){
			prst.setString(1, dataEntrada);
			prst.setString(2, dataSaida);
			prst.setDouble(3, reserva.valor(dataEntrada, dataSaida));
			prst.setString(4, formaPagamento);
			prst.setInt(5, id);
			prst.execute();
		
			JOptionPane.showMessageDialog(null, "Sucesso");
			
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public void deletar(int idReserva) {
		String querySql = "DELETE FROM reservas WHERE id = ?";
		
		try(PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.setInt(1, idReserva);
			prst.execute();
			JOptionPane.showMessageDialog(null, "Reserva Deletada com Sucesso !!");
			
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Ops ocorreu um problema, pode ser que tenha um hospede nessa reserva !!");
			throw new RuntimeException(e);
		}
	}
}

