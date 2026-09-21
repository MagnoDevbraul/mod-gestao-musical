package br.com.mod.gestaomusical.service;

import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.entity.UsuarioSetor;
import br.com.mod.gestaomusical.repository.UsuarioRepository;
import br.com.mod.gestaomusical.repository.UsuarioSetorRepository;
import br.com.mod.gestaomusical.security.UsuarioSessaoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AutorizadorMusicalService {

    private static final Set<String> PERFIS_QUE_PODEM_INDICAR_TERCEIRO =
            Set.of(
                    "SECRETARIA",
                    "ENCARREGADO_REGIONAL"
            );

    private final UsuarioRepository usuarioRepository;
    private final UsuarioSetorRepository usuarioSetorRepository;
    private final UsuarioSessaoService usuarioSessaoService;

    public AutorizadorMusicalService(
            UsuarioRepository usuarioRepository,
            UsuarioSetorRepository usuarioSetorRepository,
            UsuarioSessaoService usuarioSessaoService) {

        this.usuarioRepository = usuarioRepository;
        this.usuarioSetorRepository = usuarioSetorRepository;
        this.usuarioSessaoService = usuarioSessaoService;
    }

    /**
     * Resolve quem deve ser registrado como autorizador musical.
     *
     * A autoria da operação continua pertencendo sempre ao usuário
     * autenticado. Este método trata exclusivamente o campo
     * "Autorizado por" dos lançamentos de progresso musical.
     */
    @Transactional(readOnly = true)
    public Usuario resolverAutorizador(
            Usuario usuarioAutenticado,
            Long autorizadorInformadoId) {

        /*
         * Quando nenhum autorizador é informado, o próprio usuário
         * autenticado é utilizado como autorizador.
         */
        if (autorizadorInformadoId == null) {
            return usuarioAutenticado;
        }

        /*
         * Informar explicitamente o próprio usuário também é permitido
         * para qualquer perfil.
         */
        if (usuarioAutenticado.getId()
                .equals(autorizadorInformadoId)) {

            return usuarioAutenticado;
        }

        String perfil =
                usuarioAutenticado
                        .getPerfilUsuario()
                        .getNome()
                        .toUpperCase();

        /*
         * Somente Secretaria e Encarregado Regional podem registrar
         * um lançamento indicando outra pessoa como autorizadora.
         *
         * Instrutor(a), Encarregado Local e qualquer outro perfil
         * devem utilizar obrigatoriamente a si próprios.
         */
        if (!PERFIS_QUE_PODEM_INDICAR_TERCEIRO.contains(perfil)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Este perfil não pode indicar outro usuário como autorizador"
            );
        }

        Usuario autorizador = usuarioRepository
                .findById(autorizadorInformadoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário informado como autorizador não encontrado"
                ));

        if (!Boolean.TRUE.equals(autorizador.getAtivo())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O usuário informado como autorizador está inativo"
            );
        }

        validarMesmoSetor(
                usuarioAutenticado,
                autorizador
        );

        /*
         * Se o usuário indicado já estiver utilizando o MOD,
         * ele deverá realizar o lançamento com a própria conta.
         */
        if (usuarioSessaoService.estaOnline(
                autorizador.getEmail())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O usuário informado como autorizador está ativo no sistema. "
                            + "O lançamento deve ser realizado pelo próprio usuário."
            );
        }

        return autorizador;
    }

    private void validarMesmoSetor(
            Usuario usuarioAutenticado,
            Usuario autorizador) {

        Set<Long> setoresUsuarioAutenticado =
                obterIdsSetores(
                        usuarioAutenticado.getId()
                );

        /*
         * A ausência de setor no usuário autenticado representa
         * uma inconsistência cadastral que impede a validação segura.
         */
        if (setoresUsuarioAutenticado.isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O usuário autenticado não possui vínculo com setor"
            );
        }

        Set<Long> setoresAutorizador =
                obterIdsSetores(
                        autorizador.getId()
                );

        boolean mesmoSetor =
                setoresUsuarioAutenticado
                        .stream()
                        .anyMatch(
                                setoresAutorizador::contains
                        );

        if (!mesmoSetor) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O usuário informado como autorizador não pertence "
                            + "ao mesmo setor do usuário autenticado."
            );
        }
    }

    private Set<Long> obterIdsSetores(
            Long usuarioId) {

        return usuarioSetorRepository
                .findByUsuario_Id(usuarioId)
                .stream()
                .map(UsuarioSetor::getSetor)
                .map(setor -> setor.getId())
                .collect(Collectors.toSet());
    }
}