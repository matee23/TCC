/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.foodexpress.model.dao;

import com.foodexpress.model.Argon2Encoder;
import com.foodexpress.model.dto.TokenVerificacaoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TokenVerificacaoDAO {
    private Connection conn;

    private TokenVerificacaoDTO token;

    TokenVerificacaoDAO(Connection conn) {
        this.conn = conn;
    }

    private TokenVerificacaoDAO() {}
    
    public void addToken(TokenVerificacaoDTO token) {
        Argon2Encoder encoder = Argon2Encoder.getEncoder();
        
        String sql = "INSERT INTO token_verificacao VALUES(?, ?)";
        
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, token.getEmailUsuario());
            st.setString(2, encoder.encode(token.getToken()));
            
            int linhasAfetadas = st.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet rs = st.getGeneratedKeys();
                ConexaoBD.closeResultSet(rs);
            } else {
                throw new SQLException("Erro inesperado, nehuma linha foi afetada!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TokenVerificacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean validateToken(TokenVerificacaoDTO token) {
        Argon2Encoder encoder = Argon2Encoder.getEncoder();
        
        String sql = "SELECT * FROM token_verificacao WHERE usuario_id = ?";
            
        ResultSet r = null;
        
        TokenVerificacaoDTO tokenVerificacao = null;
        
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, token.getEmailUsuario());
            
            r = st.executeQuery();
            while (r.next()) {
                tokenVerificacao = new TokenVerificacaoDTO(r.getString("usuario_id"), r.getString("token"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(!encoder.check(token.getToken(), tokenVerificacao.getToken()) || tokenVerificacao == null) {
                System.out.println("Token não encontrado");
                return false;
            }
            return closeVerifiedToken(tokenVerificacao);
        }
    }
    
    private boolean closeVerifiedToken(TokenVerificacaoDTO token) {
        
        String sql = "DELETE FROM token_verificacao WHERE usuario_id = ?";
        
        int affectedLines = 0;
        
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, token.getEmailUsuario());
            
            System.out.println("closeVerifiedToken() " + token.getEmailUsuario());
            
            affectedLines = st.executeUpdate();
                    
            System.out.println("closeVerifiedToken(): affectedLines" + affectedLines);
        } catch (SQLException ex) {
            Logger.getLogger(TokenVerificacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return affectedLines > 0;
        }
    }
}
