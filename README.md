# Mystical Automation Omni IO

Mod de compatibilidade para:

- Minecraft 26.1.2
- NeoForge 26.1.2.76
- Mystical Automation 2.0.6
- Mystical Agriculture 9.0.x
- Cucumber 9.0.x

O mod substitui o handler lateral das sete maquinas do Mystical Automation por
um handler que:

- aceita entrada dos slots de processo por qualquer lado;
- permite extracao dos slots de saida por qualquer lado;
- nao expoe o slot de combustivel para automacao externa;
- faz Crafter, Infusion Altarnator, Awakening Altarnator e Enchanternator
  procurarem receita pelo inventario real, sem depender dos slots fantasma da
  receita.
- no Enchanternator, assume automaticamente o maior nivel de encantamento
  possivel com os materiais presentes, roteando material principal para o slot
  0, `experience_essence` para o slot 1 e livro/item encantavel para o slot 2.
- no Awakening Altarnator, roteia o slot 0 como altar, 2/4/6/8 como pedestais
  e 1/3/5/7 como essencias.

Para insercao generica por `ResourceHandler.insert(resource, amount, tx)`, o
handler distribui itens nos slots de processo em vez de encher somente o
primeiro slot. Isso melhora a integracao com exporters como o Refined Storage.

## Compilar

No Windows:

```powershell
.\gradlew.bat build
```

O JAR sera criado em `build/libs`.
