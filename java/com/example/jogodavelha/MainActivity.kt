package com.example.jogodavelha

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jogodavelha.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var difficultySwitch: Switch

    // Matriz que representa o tabuleiro do jogo
    val tabuleiro = arrayOf(
        arrayOf("A", "B", "C"),
        arrayOf("D", "E", "F"),
        arrayOf("G", "H", "I")
    )

    // Variável que armazena qual jogador está jogando, começando com "Basquete"
    var jogadorAtual = "Basquete"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Configuração do switch de dificuldade
        difficultySwitch = findViewById(R.id.difficultySwitch)
        difficultySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                difficultySwitch.text = "Dificuldade: Muito Difícil"
            } else {
                difficultySwitch.text = "Dificuldade: Fácil"
            }
        }
    }

    // Função que trata os cliques nos botões do tabuleiro
    fun buttonClick(view: View) {
        val buttonSelecionado = view as Button

        // Mapeamento do botão clicado para a posição correspondente na matriz
        when (buttonSelecionado.id) {
            binding.buttonZero.id -> tabuleiro[0][0] = jogadorAtual
            binding.buttonUm.id -> tabuleiro[0][1] = jogadorAtual
            binding.buttonDois.id -> tabuleiro[0][2] = jogadorAtual
            binding.buttonTres.id -> tabuleiro[1][0] = jogadorAtual
            binding.buttonQuatro.id -> tabuleiro[1][1] = jogadorAtual
            binding.buttonCinco.id -> tabuleiro[1][2] = jogadorAtual
            binding.buttonSeis.id -> tabuleiro[2][0] = jogadorAtual
            binding.buttonSete.id -> tabuleiro[2][1] = jogadorAtual
            binding.buttonOito.id -> tabuleiro[2][2] = jogadorAtual
        }

        // Atualiza a aparência do botão clicado e o desabilita
        buttonSelecionado.setBackgroundResource(R.drawable.basquete)
        buttonSelecionado.isEnabled = false

        // Verifica se há um vencedor após a jogada do jogador
        var vencedor = verificaVencedor(tabuleiro)

        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Alterna para o jogador "Futebol"
        jogadorAtual = if (jogadorAtual == "Basquete") "Futebol" else "Basquete"

        // Realiza a jogada do computador baseada na dificuldade selecionada
        if (difficultySwitch.isChecked) {
            jogadaMuitoDificil()
        } else {
            jogadaFacil()
        }

        // Alterna de volta para o jogador "Basquete"
        jogadorAtual = if (jogadorAtual == "Basquete") "Futebol" else "Basquete"

        // Verifica novamente se há um vencedor após a jogada do computador
        vencedor = verificaVencedor(tabuleiro)

        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Função que realiza uma jogada fácil para o computador
    private fun jogadaFacil() {
        var rX: Int
        var rY: Int
        var i = 0
        while (i < 9) {
            rX = Random.nextInt(0, 3)
            rY = Random.nextInt(0, 3)
            if (tabuleiro[rX][rY] != "Basquete" && tabuleiro[rX][rY] != "Futebol") {
                tabuleiro[rX][rY] = "Futebol"
                atualizaTabuleiro(rX, rY)
                break
            }
            i++
        }
    }

    // Função que realiza uma jogada difícil para o computador utilizando o algoritmo MiniMax
    private fun jogadaMuitoDificil() {
        val melhorMovimento = melhorMovimento(tabuleiro)
        if (melhorMovimento != null) {
            tabuleiro[melhorMovimento.first][melhorMovimento.second] = "Futebol"
            atualizaTabuleiro(melhorMovimento.first, melhorMovimento.second)
        }
    }

    // Atualiza a aparência do botão correspondente à posição no tabuleiro
    private fun atualizaTabuleiro(rX: Int, rY: Int) {
        val posicao = rX * 3 + rY
        when (posicao) {
            0 -> {
                binding.buttonZero.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonZero.isEnabled = false
            }
            1 -> {
                binding.buttonUm.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonUm.isEnabled = false
            }
            2 -> {
                binding.buttonDois.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonDois.isEnabled = false
            }
            3 -> {
                binding.buttonTres.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonTres.isEnabled = false
            }
            4 -> {
                binding.buttonQuatro.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonQuatro.isEnabled = false
            }
            5 -> {
                binding.buttonCinco.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonCinco.isEnabled = false
            }
            6 -> {
                binding.buttonSeis.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonSeis.isEnabled = false
            }
            7 -> {
                binding.buttonSete.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonSete.isEnabled = false
            }
            8 -> {
                binding.buttonOito.setBackgroundResource(R.drawable.bolafutebolistas)
                binding.buttonOito.isEnabled = false
            }
        }
    }

    // Função que determina o melhor movimento para o computador utilizando o algoritmo MiniMax
    private fun melhorMovimento(tabuleiro: Array<Array<String>>): Pair<Int, Int>? {
        var melhorPontuacao = Int.MIN_VALUE
        var movimento: Pair<Int, Int>? = null

        for (i in tabuleiro.indices) {
            for (j in tabuleiro[i].indices) {
                if (tabuleiro[i][j] != "Basquete" && tabuleiro[i][j] != "Futebol") {
                    val temp = tabuleiro[i][j]
                    tabuleiro[i][j] = "Futebol"
                    val pontuacao = minimax(tabuleiro, 0, false)
                    tabuleiro[i][j] = temp
                    if (pontuacao > melhorPontuacao) {
                        melhorPontuacao = pontuacao
                        movimento = Pair(i, j)
                    }
                }
            }
        }
        return movimento
    }

    // Implementação do algoritmo MiniMax para determinar a melhor jogada
    private fun minimax(tabuleiro: Array<Array<String>>, depth: Int, isMaximizing: Boolean): Int {
        val vencedor = verificaVencedor(tabuleiro)
        if (vencedor != null) {
            return when (vencedor) {
                "Futebol" -> 1
                "Basquete" -> -1
                "Empate" -> 0
                else -> 0
            }
        }

        return if (isMaximizing) {
            var melhorPontuacao = Int.MIN_VALUE
            for (i in tabuleiro.indices) {
                for (j in tabuleiro[i].indices) {
                    if (tabuleiro[i][j] != "Basquete" && tabuleiro[i][j] != "Futebol") {
                        val temp = tabuleiro[i][j]
                        tabuleiro[i][j] = "Futebol"
                        val pontuacao = minimax(tabuleiro, depth + 1, false)
                        tabuleiro[i][j] = temp
                        melhorPontuacao = maxOf(melhorPontuacao, pontuacao)
                    }
                }
            }
            melhorPontuacao
        } else {
            var melhorPontuacao = Int.MAX_VALUE
            for (i in tabuleiro.indices) {
                for (j in tabuleiro[i].indices) {
                    if (tabuleiro[i][j] != "Basquete" && tabuleiro[i][j] != "Futebol") {
                        val temp = tabuleiro[i][j]
                        tabuleiro[i][j] = "Basquete"
                        val pontuacao = minimax(tabuleiro, depth + 1, true)
                        tabuleiro[i][j] = temp
                        melhorPontuacao = minOf(melhorPontuacao, pontuacao)
                    }
                }
            }
            melhorPontuacao
        }
    }

    // Função que verifica se há um vencedor no tabuleiro
    fun verificaVencedor(tabuleiro: Array<Array<String>>): String? {
        for (i in 0 until 3) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
                return tabuleiro[i][0]
            }
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
                return tabuleiro[0][i]
            }
        }
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            return tabuleiro[0][0]
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            return tabuleiro[0][2]
        }
        var empate = 0
        for (linha in tabuleiro) {
            for (valor in linha) {
                if (valor == "Basquete" || valor == "Futebol") {
                    empate++
                }
            }
        }
        return if (empate == 9) "Empate" else null
    }
}
