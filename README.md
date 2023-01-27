# Shows List #
____
#### Overview:
 *Develop an application to list TV series using the TVMaze API. This is the API URL: https://www.tvmaze.com/api*
____

<img width="300" src="https://user-images.githubusercontent.com/96268732/179521148-5861c8aa-05c8-43cb-88ed-1aaa8b1e79a4.png">

#### SUA API PERFEITA PRA DAR AQUELE ROLÉ!
___
#### 📝 Descrição:
*Estou desenvolvendo uma aplicação pra localizar lugares legais pra dar aquele agito no final de semana! Utilizando essa aplicação é possível: organizar sua lista de lugares como restaurantes, bares, cadastro de clientes com o CRUD completo!*
____
#### ⚙️ Dependências:
[Android Youtube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player)
#### 🦶 Para iniciar esse projeto siga os passos:
- Crie uma pasta no seu computador para armazenar o projeto e entre nela
- Dentro da pasta, clique com o direito do mouse e selecione "Git Bash Here"
- Com o terminal aberto, digite no terminal "git init"
- Depois é só copiar o código abaixo e colar no terminal para fazer o clone desse repositório e instalar as dependências

```
git clone https://github.com/cesarfreitax/roleApi.git && cd ProjetoFinalModulo4 && npm install
```
____
### ⚙️ Depêndecias de desenvolvimento:
```js 
"devDependencies": {
    "dotenv": "^16.0.0",
    "jest": "^28.0.3"
  }
  ```
____
## 🚀 Para utilizar a aplicação 
Inicie o terminal e rode com: 
```js
npm start
```

____
# ➡️ Rotas:

Nossa API por enquanto conta com apenas uma entidade e suas próprias rotas:

## 📍 Lugares:

### 👀 Ver todo os lugares, pra dar um rolé, no banco de dados:
- Método GET 
- No caminho "url da API" + /lugares
#### URL:
```
http://localhost:3000/lugares
```


### 🔎 Buscar apenas um lugar, pra dar um rolé, no banco de dados por id:
- Método GET 
- No caminho "url da API" + /lugares/:id
#### URL:
```
http://localhost:3000/lugar/1
```


### ➕ Adicionar novo lugar, pra dar um rolé, no banco de dados:
- Método POST 
- No caminho "url da API" + /lugar
- Colocar no corpo da requisicao os dados necessários para adicionar um lugar
#### URL:
```
http://localhost:3000/lugar
```
#### Corpo da requisição:
``` js:
{
	"nome_do_lugar": "Sushi da Praca",
	"bairro": "Barra da Tijuca",
	"descricao": "Restaurante especializado em comida japonesa.",
	"link": "http://www.sushidapraca.com.br/"
}
```

### ↩️ Alterar cardápio:
- Método PUT
- No caminho "url da API" + /lugar/:id
- Colocar no corpo da requisicao os dados necessários para alterar um lugar
#### URL:
```
http://localhost:3000/lugar/1
```
#### Corpo da requisição:
``` js:
{
	"nome_do_lugar": "Algum nome",
	"bairro": "Algum bairro",
	"descricao": "Alguma descricao.",
	"link": "http://www.algumlink.com.br/"
}
```

### ❌ Deletar item no cardápio:
- Método DELETE
- No caminho "url da API" + /lugar/id
#### URL:
```
http://localhost:3000/lugar/1
```
____
#### 🌐 Heroku:
Fiz o deploy na plataforma de núvem Heroku, pois é a que estou mais acostumado no momento.

Veja a aplicação online: 

[Heroku.](https://role-api.herokuapp.com/) 
Lembre-se de adicionar a Rota que deseja..
Ex: 
``` js
https://role-api.herokuapp.com/lugares 
```
____

#### Versão:
```js 
node: v16.14.2
npm: 8.5.0
