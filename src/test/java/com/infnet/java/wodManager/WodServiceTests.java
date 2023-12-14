package com.infnet.java.wodManager;

import com.infnet.java.wodManager.exception.ResourceNotFoundException;
import com.infnet.java.wodManager.model.Wod;
import com.infnet.java.wodManager.service.WodService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WodServiceTests {
    Logger logger = LoggerFactory.getLogger(WodServiceTests.class);
    @Autowired
    WodService wodService;
    @Test
    public void testaGetAll(){
        logger.info("Iniciando testaGetAll");
        List<Wod> wods = wodService.getAll();
        assertEquals(7, wods.size());
        logger.info("testaGetAll concluído com sucesso");
    }

    @Test
    public void testaGetByIdExiste(){
        logger.info("Iniciando testaGetByIdExiste");
        int id = 1;
        try {
            Wod wod = wodService.getById(id);
            assertNotNull(wod);
            logger.info("Wod com ID {} encontrado.", id);
        } catch (ResourceNotFoundException e) {
            logger.error("Wod com ID {} não encontrado.", id, e);
            fail("Wod deveria existir");
        }
    }

    @Test
    public void testaGetByIdNaoExiste(){
        logger.info("Iniciando testaGetByIdNaoExiste");
        int nonexistentId = 10;
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            wodService.getById(nonexistentId);
        });
        logger.info("Exceção esperada ResourceNotFoundException foi lançada.");
    }

    @Test
    public void testaDeletarById(){
        logger.info("Testando deletar o Wod pelo ID");
        int idExistente = 1;
        try {
            Wod wod = wodService.getById(idExistente);
            assertNotNull(wod, "Wod deve existir antes de ser deletado.");
            Wod wodRemovido = wodService.deletarById(idExistente);
            assertEquals(wod, wodRemovido, "O Wod deletado deve ser o mesmo que foi buscado.");
            logger.info("Wod com ID {} deletado com sucesso", idExistente);

            assertThrows(ResourceNotFoundException.class, () -> wodService.getById(idExistente));
            logger.info("Verificação pós-exclusão passou. Wod com ID {} não encontrado como esperado.", idExistente);
        } catch (Exception e) {
            logger.error("Erro durante o teste de exclusão do Wod pelo ID", e);
            fail("Erro durante o teste de exclusão");
        }
    }


    @Test
    public void testGetWodsByCategoriaETipo_AmbosNull() {
        logger.info("Testando getWodsByCategoriaETipo com ambos os parâmetros como null");
        List<Wod> resultado = wodService.getWodsByCategoriaETipo(null, null);
        assertEquals(7, resultado.size());
        logger.info("A busca retornou o número esperado de Wods: {}", resultado.size());
    }

    @Test
    public void testGetWodsByCategoriaETipo_ApenasCategoria() {
        logger.info("Testando getWodsByCategoriaETipo com apenas a categoria");
        String categoriaTeste = "Hero Wod";
        List<Wod> resultado = wodService.getWodsByCategoriaETipo(categoriaTeste, null);
        assertTrue(resultado.stream().allMatch(wod -> wod.getCategoria().equalsIgnoreCase(categoriaTeste)));
        logger.info("A busca por categoria retornou Wods corretamente filtrados pela categoria: {}", categoriaTeste);
    }

    @Test
    public void testGetWodsByCategoriaETipo_ApenasTipo() {
        logger.info("Testando getWodsByCategoriaETipo com apenas o tipo");
        String tipoTeste = "For Time";
        List<Wod> result = wodService.getWodsByCategoriaETipo(null, tipoTeste);
        assertTrue(result.stream().allMatch(wod -> wod.getTipo().equalsIgnoreCase(tipoTeste)));
        logger.info("A busca por tipo retornou Wods corretamente filtrados pelo tipo: {}", tipoTeste);
    }

    @Test
    public void testGetWodsByCategoriaETipo() {
        logger.info("Testando getWodsByCategoriaETipo com categoria e tipo");
        String categoriaTeste = "Hero Wod";
        String tipoTeste = "For Time";
        List<Wod> result = wodService.getWodsByCategoriaETipo(categoriaTeste, tipoTeste);
        assertTrue(result.stream().allMatch(wod -> wod.getCategoria().equalsIgnoreCase(categoriaTeste)
                && wod.getTipo().equalsIgnoreCase(tipoTeste)));
        logger.info("A busca por categoria e tipo retornou Wods corretamente filtrados.");
    }

    @Test
    public void testIncrementarId(){
        logger.info("Testando incremento de ID");
        int idIncrementadoEsperado = 8;
        assertEquals(idIncrementadoEsperado, wodService.incrementarId());
        logger.info("ID incrementado como esperado para: {}", idIncrementadoEsperado);
    }

    @Test
    void testCriarWod() {
        logger.info("Testando a criação de um novo Wod");
        List<String> descricao = Arrays.asList("10 Rounds:", "8 Ground to overheads 95/65lbs", "10 Bar facing burpees");
        LocalDate data = LocalDate.of(2022, 7, 29);
        Wod novoWod = new Wod(0, "Open 20.1", "For Time", 15, "Open", descricao, "14min 28seg", data, null);
        wodService.criarWod(novoWod);
        assertEquals(8, novoWod.getId());
        assertEquals(8, wodService.getAll().size());
        int tamanhoListaWods = wodService.getAll().size();
        Wod wodAdicionado = wodService.getAll().get(tamanhoListaWods - 1);
        assertEquals("Open 20.1", wodAdicionado.getNome());
        assertEquals("For Time", wodAdicionado.getTipo());
        assertEquals(15, wodAdicionado.getDuracaoMinutos());
        logger.info("Wod criado com sucesso com ID {}", novoWod.getId());
    }

    @Test
    void testAlterarWod(){
        logger.info("Iniciando testAlterarWod");
        LocalDate data = LocalDate.of(2022, 7, 29);
        Wod wodDetails = new Wod(1, "Novo Nome", "Novo Tipo", 20, "Nova Categoria", Arrays.asList("Nova Descrição"), "Novo Score", data, null);
        try {
            Wod updatedWod = wodService.alterarWod(1, wodDetails);
            assertNotNull(updatedWod);
            assertEquals(wodDetails.getNome(), updatedWod.getNome());
            logger.info("Wod com ID {} atualizado com sucesso.", updatedWod.getId());
        } catch (ResourceNotFoundException e) {
            logger.error("Falha ao atualizar Wod com ID {}. Wod não encontrado.", wodDetails.getId(), e);
            fail("Falha ao atualizar Wod");
        } catch (Exception e) {
            logger.error("Erro ao atualizar Wod com ID {}.", wodDetails.getId(), e);
            fail("Erro inesperado ao atualizar Wod");
        }
    }

}
