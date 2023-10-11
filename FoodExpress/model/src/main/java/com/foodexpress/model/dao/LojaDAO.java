/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.foodexpress.model.dao;

import com.foodexpress.model.dto.LojaDTO;
import com.foodexpress.model.dto.ProdutoDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class LojaDAO extends DAOTemplate<LojaDTO> {
    private static LojaDAO instance = null;
    
    private LojaDTO loja;
    
    private LojaDAO() {
        super();
    }
    
    public static synchronized LojaDAO getInstance(){
        if(instance == null)
            instance = new LojaDAO();
        
        return instance;
    }
    
    @Override
    protected LojaDTO mapResultSetToObject(ResultSet rs) throws SQLException {
        LojaDTO loja = null;
        
        try{
            loja = new LojaDTO();
            
            loja.setId(rs.getInt("id"));
            loja.setNome(rs.getString("nome"));
            loja.setDescricao(rs.getString("descricao"));
            loja.setAvaliacao(rs.getDouble("avaliacao"));
            loja.setIdUser(rs.getString("id_usuario"));
            loja.setQtdAvaliacoes(rs.getInt("qtd_avaliacoes"));
            loja.setSomaAvaliacoes(rs.getInt("soma_avaliacoes"));
        } catch(SQLException ex){
            java.util.logging.Logger.getLogger(LojaDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return loja;
    }
    
    public boolean cadastrar(LojaDTO obj){
        String sql = "INSERT INTO lojas (nome, descricao, avaliacao, id_usuario) VALUES (?, ?, ?, ?)";
        
        return executeUpdate(sql, obj.getNome(), obj.getDescricao(), obj.getAvaliacao(), obj.getIdUser());
    }
    
    public int login(String idUser){
        String sql = "SELECT * FROM lojas WHERE id_usuario = ?";
        
        List<LojaDTO> lojas = executeQuery(sql, idUser);
        
        //Loja não cadastrada
        if(lojas.isEmpty())
            return -1;
        
        LojaDTO loja = lojas.get(0);
        
        return 1;
    }
    
    public LojaDTO getLoja(String idUser) {
        String sql = "SELECT * FROM lojas WHERE id_usuario = ?";
        
        List<LojaDTO> lojas = executeQuery(sql, idUser);
        
        return lojas.isEmpty() ? null : lojas.get(0);
    }
    
    public LojaDTO getLojaById(int idLoja) {
        String sql = "SELECT * FROM lojas WHERE id = ?";
        
        List<LojaDTO> lojas = executeQuery(sql, idLoja);
        
        return lojas.isEmpty() ? null : lojas.get(0);
    }
    
    public boolean updateND(LojaDTO obj){
        String sqlUpdate = "UPDATE lojas SET nome = ?, descricao = ? WHERE id_usuario = ?";
        
        return executeUpdate(sqlUpdate, obj.getNome(), obj.getDescricao(), obj.getIdUser());
    }
    
    public boolean updateAvaliacao(LojaDTO obj){
        String sqlUpdate = "UPDATE lojas SET avaliacao = ?, qtd_avaliacoes = ?, soma_avaliacoes = ? WHERE id = ?";
        
        return executeUpdate(sqlUpdate, obj.getAvaliacao(), obj.getQtdAvaliacoes(), obj.getSomaAvaliacoes(), obj.getId());
    }
    
    public List<LojaDTO> ListarLojas() {
        String sql = "SELECT * FROM lojas ORDER BY nome";
        
        return executeQuery(sql);
    }
    
    public LojaDTO getLoja(){
        return loja;
    }
}
