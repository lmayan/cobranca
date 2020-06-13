// Inicializa o MODAL de confirmacao de Exclusao
$('#confirmacaoExclusaoModal').on('show.bs.modal', function(event){
	
	var button = $(event.relatedTarget); //event.relatedTarget indica qual botao/componente disparou o EVENT
	
	var codigoTitulo = button.data('codigo'); // Recupera o atributo 'data-codigo' criado do th:attr na pagina 
	var descricaoTitulo = button.data('descricao'); // Recupera o atributo 'data-descricao' criado do th:attr na pagina 
	
	var modal = $(this); //Pega o componente(modal) que disparou o evento
	var form = modal.find('form');// Pega o <form> de dentro do modal
	var action = form.attr('data-url-base');// Pega a <action> de dentro do <form>
	
	if(!action.endsWith('/')){ // (SE não terminar com '/') adicione a '/' 
		action += '/';
	}
	
	form.attr('action', action + codigoTitulo +'/deletar');//Edita a action adicionando o codigoTitulo e CONTROLLER
	//Encontra o <span> dentro da class='modal-body' e edita a estrutura do componente
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o titulo ID: <strong>'
			+codigoTitulo+' '+descricaoTitulo+'</strong>?')
	
	//alert para confirmar recebimento do atributo que sera usado para exclusao 
	//alert(codigoTitulo);
	
});

// Inicializa as funcoes abaixo do JavaScript ao carregar pagina
$(function () {
	
	//Inicializa comp. 'tooltip' Bootstrap possuem a tag 'rel="tooltip"'
	$('[rel="tooltip"]').tooltip(); 
	
	//Inicializa as mascaras do 'maskMoney'
	$('.js-currency').maskMoney({thousands:'.', decimal:',', allowZero:false});

	//Executa funcao qdo clicar sobre componente que possua class="js-atualizar-status" 
	$('.js-atualizar-status').on('click', function(event){ 

		event.preventDefault(); //Faz que o componente nao execute de forma PADRAO (Fazendo requisicao)
		
		var btnReceber = $(event.currentTarget); //Variavel para receber o componente disparado (botao clicado).
		var urlReceber = btnReceber.attr('href'); //Variavel para receber caminho da URL contido no objeto
		
		//console.log('clicou'); //Teste EVENT no console
		//console.log('Conteudo de urlReceber: ',urlReceber);
		
		//Chamando AJAX via JQuery
		var response = $.ajax({
			url: urlReceber, //Define o caminho da URL 
			type: 'PUT' //Define o metodo da requisicao (no caso PUT para editar o status)
		});
		
		
		// Depois de chamar AJAX se RESPOSTA tiver SUCESS, executa isso!!
		response.done(function(resp){
			// Recupera informacao do codigo que esta no botao receber
			var codigoTitulo = btnReceber.data('codigo');
			
			// Captura o componente que tem a marcacao 'data-status-linha' e faz alteracao no componente <span>
			// que esta dentro dele modificando o seu conteudo via html.
			// Recebe tambem o retorno do metodo "atualizarStatus()" para edicao da descricao do <spans>
			$('[data-status-linha=' + codigoTitulo + ']').html('<span class="label label-success">'+ resp +'</span>')
			
			// Esconde o botão de receber
			btnReceber.hide();
			
			//Teste Recebimento INFO
			//alert("SUCESSO -  ao Receber a cobrança.");
		});
		
		// Depois de chamar AJAX se RESPOSTA tiver FAIL, executa isso!!
		response.fail(function(e){
			console.log(e);
			alert("Erro ao Receber a cobrança.");
		});
		
		
	});
	
});




