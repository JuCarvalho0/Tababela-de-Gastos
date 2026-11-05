const API = 'http://localhost:8080/api/transacoes';

const fmt = v => Number(v).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

const tabelaBody = document.getElementById('tabelaBody');
const msg = document.getElementById('msg');

function mostrarMsg(texto, tipo = 'ok') {
  msg.textContent = texto;
  msg.className = 'msg ' + tipo;
  setTimeout(() => { msg.textContent = ''; msg.className = 'msg'; }, 3500);
}

async function carregarTudo() {
  try {
    const [resTrans, resResumo] = await Promise.all([
      fetch(API),
      fetch(`${API}/resumo`)
    ]);

    const transacoes = await resTrans.json();
    const resumo = await resResumo.json();

    renderizarTabela(transacoes);
    renderizarResumo(resumo);
  } catch (e) {
    console.error(e);
    tabelaBody.innerHTML = `<tr><td colspan="5" class="vazio">
      ⚠️ Não foi possível conectar ao backend. Verifique se o Spring Boot está rodando em http://localhost:8080
    </td></tr>`;
  }
}

function renderizarResumo(r) {
  document.getElementById('totalReceitas').textContent = fmt(r.receitas);
  document.getElementById('totalDespesas').textContent = fmt(r.despesas);
  const saldoEl = document.getElementById('totalSaldo');
  saldoEl.textContent = fmt(r.saldo);
  saldoEl.style.color = Number(r.saldo) >= 0 ? 'var(--rosa)' : 'var(--vermelho)';
}

function renderizarTabela(lista) {
  if (!lista || lista.length === 0) {
    tabelaBody.innerHTML = `<tr><td colspan="5" class="vazio">
      Nenhuma transação ainda. Adicione a primeira! 🚀
    </td></tr>`;
    return;
  }

  tabelaBody.innerHTML = lista.map(t => {
    const data = new Date(t.data + 'T00:00:00').toLocaleDateString('pt-BR');
    const receita = t.tipo === 'RECEITA';
    return `
      <tr>
        <td>${escapeHtml(t.descricao)}</td>
        <td><span class="tag ${receita ? 'receita' : 'despesa'}">
          ${receita ? 'Receita' : 'Despesa'}
        </span></td>
        <td>${data}</td>
        <td style="color:${receita ? 'var(--verde)' : 'var(--vermelho)'}; font-weight:600;">
          ${receita ? '+' : '-'} ${fmt(t.valor)}
        </td>
        <td><button class="btn-del" onclick="remover(${t.id})">Remover</button></td>
      </tr>
    `;
  }).join('');
}

function escapeHtml(s) {
  const d = document.createElement('div');
  d.textContent = s;
  return d.innerHTML;
}

document.getElementById('formTransacao').addEventListener('submit', async (e) => {
  e.preventDefault();

  const payload = {
    descricao: document.getElementById('descricao').value.trim(),
    valor: parseFloat(document.getElementById('valor').value),
    tipo: document.getElementById('tipo').value,
    data: document.getElementById('data').value
  };

  if (!payload.descricao || !payload.valor || !payload.data) {
    mostrarMsg('Preencha todos os campos.', 'erro');
    return;
  }

  try {
    const res = await fetch(API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Erro ao salvar');
    }

    document.getElementById('formTransacao').reset();
    mostrarMsg('Transação salva com sucesso! ✅');
    carregarTudo();
  } catch (err) {
    mostrarMsg(err.message || 'Erro ao conectar.', 'erro');
  }
});

async function remover(id) {
  if (!confirm('Deseja remover esta transação?')) return;
  try {
    await fetch(`${API}/${id}`, { method: 'DELETE' });
    carregarTudo();
  } catch (e) {
    mostrarMsg('Erro ao remover.', 'erro');
  }
}

// Data padrão = hoje
document.getElementById('data').valueAsDate = new Date();

carregarTudo();