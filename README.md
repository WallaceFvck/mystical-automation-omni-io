# Mystical Automation Omni IO

Patch não oficial para o mod **Mystical Automation**, focado em melhorar a automação das máquinas com sistemas como Refined Storage, Applied Energistics, pipes e outros mods que usam capabilities/handlers de item do NeoForge.

## O que este mod faz

- Permite entrada e saída de itens por qualquer lado das máquinas do Mystical Automation.
- Mantém o slot de combustível fora da automação externa.
- Permite extrair apenas slots de saída.
- Remove a dependência dos slots de receita "fantasma" em máquinas compatíveis.
- Faz algumas máquinas resolverem a receita pelo inventário real.

## Compatibilidade

Testado no ambiente:

- Minecraft `26.1.2`
- NeoForge `26.1.2.76`
- Mystical Automation `2.0.6`
- Mystical Agriculture `9.0.x`
- Cucumber `9.0.x`
- Refined Storage `3.2.x`
- Modpack: All the Mods 11 / ATM11

Este mod foi feito especificamente para essa linha de versões. Outras versões podem exigir ajustes nos Mixins.

## Máquinas suportadas

### Infuser

- Entrada por qualquer lado.
- Saída por qualquer lado.
- Slot de combustível não é exposto.
- Mantém a validação original de itens do Mystical Automation.

### Crafter

- Entrada por qualquer lado.
- Saída por qualquer lado.
- Não precisa mais predefinir a receita nos slots fantasma.
- A receita é lida pelo inventário real da máquina.

Observação: receitas shaped ainda dependem da ordem correta dos itens nos slots internos.

### Farmer

- Entrada por qualquer lado.
- Saída por qualquer lado.
- Combustível não é exposto.
- Mantém a validação original dos slots de sementes/entrada.

### Fertilizer

- Entrada por qualquer lado.
- Combustível não é exposto.
- Não há saída de item, seguindo o comportamento original da máquina.

### Infusion Altarnator

- Entrada por qualquer lado.
- Saída por qualquer lado.
- Não precisa mais predefinir a receita fantasma.
- A receita é lida pelo inventário real.

### Awakening Altarnator

- Entrada por qualquer lado.
- Saída por qualquer lado.
- Não precisa mais predefinir a receita fantasma.
- Roteamento de slots ajustado:
  - slot `0`: item central/altar;
  - slots `2`, `4`, `6`, `8`: itens de pedestal;
  - slots `1`, `3`, `5`, `7`: essências.

### Enchanternator

- Entrada por qualquer lado.
- Saída por qualquer lado.
- Não precisa mais predefinir a receita fantasma.
- Assume automaticamente o maior nível de encantamento possível com os materiais presentes.
- Roteamento de slots ajustado:
  - slot `0`: material principal da receita;
  - slot `1`: `mysticalagriculture:experience_essence`;
  - slot `2`: livro ou item a ser encantado.

## Instalação

Baixe o arquivo `.jar` mais recente na página de releases:

[Releases](https://github.com/WallaceFvck/mystical-automation-omni-io/releases)

Depois coloque o `.jar` na pasta `mods` da instância:

```text
<sua-instancia>/mods
```

Para ATM11 no CurseForge, exemplo:

```text
C:\Users\<seu-usuario>\curseforge\minecraft\Instances\All the Mods 11 - ATM11\mods
```

Reinicie o jogo/modpack depois de instalar. Mixins só são aplicados no carregamento.

## Build local

No Windows:

```powershell
.\gradlew.bat clean build
```

O `.jar` será gerado em:

```text
build/libs
```

## Como funciona

O mod usa SpongeMixin para interceptar os handlers de item expostos pelas BlockEntities do Mystical Automation.

Em vez de devolver os wrappers laterais originais, ele expõe um handler próprio que:

- aceita inserção nos slots de processo;
- bloqueia inserção em slots de combustível;
- permite extração apenas dos slots de saída;
- aplica roteamento específico para máquinas com layout interno rígido;
- ignora a validação antiga contra o inventário fantasma quando a máquina suporta receita dinâmica.

## Limitações conhecidas

- Este é um patch não oficial.
- Atualizações do Mystical Automation podem mudar nomes de classes, métodos ou layouts de slots e quebrar os Mixins.
- Receitas shaped do Crafter ainda dependem da ordem em que o sistema de automação insere os itens.
- O comportamento foi pensado principalmente para integração com Refined Storage Exporter e handlers de item do NeoForge.

## Licença

MIT.
