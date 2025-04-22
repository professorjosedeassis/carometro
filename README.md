![GitHub](https://img.shields.io/github/license/professorjosedeassis/carometro)
# JavaGRAM - Projeto Carômetro
Neste projeto prático, você vai criar um Carômetro Digital, o famoso mural de carinhas da turma, só que versão tech, com interface gráfica em Java, banco de dados MySQL e direito até a upload de fotos dos alunos.

É quase um Instagram raiz da sala de aula: sem stories, sem filtro... só código puro e funcional!

😃 Porque foto 3x4 é coisa do passado!

![carometro](https://github.com/professorjosedeassis/carometro/blob/main/assets/caraometro1.png)
## Autor
Professor José de Assis
## Instruções para instalação e uso do aplicativo
### Pré requisitos
1) Ter o Java **versão 17** ou superior instalado. Testado com a versão openJDK 21 LTS que pode ser obtida no link indicado. Na instalação selecione todos os recursos conforme indicado na imagem.

[download openJDK)](https://adoptium.net/)

![openJDK](https://github.com/professorjosedeassis/carometro/blob/main/assets/openjdk.png)

2) Ter um banco de dados local baseado no **MySQL 8** ou MariaDB compatível, no exemplo usei o XAMPP que pode ser obtido no link indicado.

[download xampp](https://www.apachefriends.org/)

### Instalação do banco
1) Iniciar os serviços Apache e MySQL no XAMPP, conforme indicado na imagem.

![xampp start](https://github.com/professorjosedeassis/carometro/blob/main/assets/xampp1.png)

2) No navegador de internet digite: **localhost/dashboard** e selecione no menu: **phpMyAdmin** conforme indicado na imagem.

![phpmyadmin](https://github.com/professorjosedeassis/carometro/blob/main/assets/xampp2.png)

3) Crie um novo banco de dados de nome **dbcarometro** (sem usar acentuação) conforme indicado na imagem.

![dbcarometro](https://github.com/professorjosedeassis/carometro/blob/main/assets/xampp3.png)

4) Na aba SQL, copie e cole o código abaixo e execute. (Passos 1,2 e 3 indicados na imagem)

~~~sql
CREATE TABLE alunos (ra int PRIMARY KEY AUTO_INCREMENT,nome varchar(30) NOT NULL,foto LONGBLOB NOT NULL);
~~~

![alunos](https://github.com/professorjosedeassis/carometro/blob/main/assets/xampp4.png)

### Instalação do aplicativo
1) Em Releases faça o download do arquivo **carometro.jar**
2) Execute e verifique no rodapé o ícone que representa o banco de dados conectado. Se estiver com erro (conforme indicado na figura) verifique o XAMPP e revise novamente os passos 1 a 4 da instalação do banco.

![app](https://github.com/professorjosedeassis/carometro/blob/main/assets/caraometro2.png)

3) Se tudo estiver OK ( 1 ) você pode iniciar gerando uma listagem dos alunos cadastrados ( 2 ) ou pesquisar um aluno pelo RA ou Nome ( 3 ), neste caso o aplicativo libera os botões e recursos de acordo com o resultado da pesquisa, por exemplo se não tiver um aluno cadastrado ele libera os botões para carregar foto e adicionar e se existir um aluno cadastrado ele traz todas as informações e libera os botões para editar e excluir.

![aplicativo](https://github.com/professorjosedeassis/carometro/blob/main/assets/caraometro3.png) 

## Tutorial passo a passo para desenvolver este projeto do "zero"
Tecnologias que são abordadas neste tutorial:
- Criação de banco de dados e tabelas no MySQL
- CRUD (Create Read Update e Delete)
- IDE Eclipse
- Java SE
- Window Builder
- JDBC (Java Database Connectivity)
- Validação de dados
- Uso da biblioteca iTextpdf para gerar listagem de alunos com foto

### Iniciar tutorial:
▶️ [Playlist](https://www.youtube.com/playlist?list=PLbEOwbQR9lqz6tSoH51QAEzpo1VsXgvIv)
### Bibliotecas usadas neste projeto
[mysql](https://dev.mysql.com/downloads/connector/j/)

[itextpdf](https://github.com/itext/itextpdf)
### Slide de apoio as aulas
[JDBC](https://professorjosedeassis.github.io/infox/)

<hr>

### ☕ Projetos sem café? Impossível!
Criar e compartilhar projetos gratuitos exige tempo, dedicação e, claro, muito café! Se quiser apoiar, um "cafezinho" faz toda a diferença. <br>Sua doação incentiva mais projetos reais e mantém a motivação lá em cima!
#### Chave PIX❖:
~~~txt
josedeassisfilho@gmail.com
~~~
*( em nome de José de Assis Filho )*

E olha, só de dar uma estrela ⭐, seguir o repositório e compartilhar, você já está dando uma baita força!

😃 Valeu demais pelo apoio!
