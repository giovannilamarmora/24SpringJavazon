$(document).ready(function () {
    
    function getProdotti() {
        $.get('prodotti', function (resume) {
            const output = $('#output');
            for (let i = 0; i < resume.length; i++) {
                $(`<tr id='riga-${resume[i].id}'>
                <td data-field="id" data-sortable="true">${resume[i].id}</td>
                <td class='nome'>${resume[i].nome}</td>
                <td class='descrizione'>${resume[i].descrizione}</td>
                <td class='prezzo'>${resume[i].prezzo}</td>
                <td class='disponibilita'>${resume[i].disponibilita}</td>
                <td>
                    <div class="btn-group" role="group">
                        <button id="btnGroupDrop1" type="button" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">Opzioni</button>
                            <ul class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                                <li><a class="dropdown-item btn-modifica" data-bs-toggle="modal" data-bs-target="#modifica" data-id='${resume[i].id}'>Modifica</a></li>
                                <li><a class="dropdown-item btn-elimina" data-id='${resume[i].id}'>Elimina</a></li>
                            </ul>
                    </div>
                </td>
            </tr>`).hide().appendTo(output).fadeIn(i * 150);
            }
          })
    }
    getProdotti();

    function addprodotto(prodotto){
        console.log(prodotto);
        $.ajax({
            type: 'POST',
            url: '/prodotti',
            data: JSON.stringify(prodotto),
            contentType: 'application/json',
            dataType: 'json',
            success: function(response){
                if (response.messaggio === "Metodo Post non Funzionante"){
                  //  let formData = new FormData(); 
                  //  formData.append("file", fileupload.files[0]);
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Something went wrong!'
                      })
                } else if (response.messaggio === "Detto, Fatto") {
                    Swal.fire({
                        icon: 'success',
                        title: 'Aggiunta',
                        text: 'Aggiunta Eseguita Correttamente',
                        showConfirmButton: false,
                        timer: 1500
                      })
                      setTimeout(function () {
                          window.location.href='prodotti.html';
                        }, 1500);
                }
            }
        })
    }
    // Immagine Visualizzazione
    $('#formFile').change(function () {
        showImage(this);
    })

    function showImage(fileInput) {
        file = fileInput.files[0];
        reader = new FileReader();

        reader.onload = function(e) {
            $('#showThumb').attr('src', e.target.result);
            $('#showThumb').css('display', 'block');
        };
        reader.readAsDataURL(file);
    }
    // Fine Immagine

    $('#aggiungiProdotto').click(function () {
        const prodotti = {
            nome: $('#nomeProdotto').val(),
            descrizione: $('#descrizione').val(),
            disponibilita: $('#disponibilita').val(),
            prezzo: $('#prezzo').val(),
            categoria: {
                id:  $('#catSelect').val()
            },
            image: $('#formFile').val()
        }
            addprodotto(prodotti);

        $('#nomeProdotto').val('');
        $('#descrizione').val('');
        $('#disponibilita').val('');
        $('#prezzo').val('');
        $('#catSelect').val();
        
    })

    $('#btnModificaConf').click(function () {
        const prodotti = {
            nome: $('#nomeProdotto').val(),
            descrizione: $('#descrizione').val(),
            disponibilita: $('#disponibilita').val(),
            prezzo: $('#prezzo').val(),
            categoria: {
                id:  $('#catSelect').val()
            }
        }
        if (editMode) {
            Swal.fire({
                icon: 'question',
                title: 'Vuoi salvare la Modifica di ' + prodotti.nome + '?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: `Salva`,
                denyButtonText: `Non Salvare`,
              }).then((result) => {
                /* Read more about isConfirmed, isDenied below */
                if (result.isConfirmed) {
                  Swal.fire('Salvato!', '', 'success')
                  prodotti.id = idModifica;
                  modificaProdotto(prodotti);
                  setTimeout(function () {
                    window.location.href='prodotti.html';
                  }, 2000);
                } else if (result.isDenied) {
                  Swal.fire('Modifiche non salvate', '', 'info')
                }
              })
              
        }
        
    })

    function getCategorieSelect() {
        $.get('categorie', function (resumeC) {
            const selectCategoria = $('#selectCategorie');
            for (let i = 0; i < resumeC.length; i++) {
                $(`<option id='catSelect' value="${resumeC[i].id}">${resumeC[i].nome}</option>`)
                .hide().appendTo(selectCategoria).fadeIn(i * 100);
            }
          })
    } 
    getCategorieSelect();

    function deleteProdotto(id) {
        let idPagina = $(`#riga-${id}`);
        
        $.ajax({
            type: "DELETE",
            url: `prodotti/${id}`,
            success: function (response) {
                console.log(response);
                if (response.messaggio === "Detto, Fatto") {
                    idPagina.slideUp(300, function () {
                        idPagina.remove();
                    });
                } else {
                    alert("Problema Durante la cancellazione. Aborted");
                }
                
            }
        });
    }

    $('#output').on('click', '.btn-elimina', function() {
        const id = $(this).attr('data-id');
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-danger',
                cancelButton: 'btn btn-primary mx-2'
            },
            buttonsStyling: false
          })
          
          swalWithBootstrapButtons.fire({
            title: 'Sei Sicuro?',
            text: "Operazione Irreversibile!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Elimina',
            cancelButtonText: 'Esci',
            reverseButtons: true
          }).then((result) => {
            if (result.isConfirmed) {
              swalWithBootstrapButtons.fire(
                'Cancellato!',
                'Il tuo Prodotto è stato Eliminato.',
                'success'
              )
              deleteProdotto(id);
            } else if (
              /* Read more about handling dismissals below */
              result.dismiss === Swal.DismissReason.cancel
            ) {
              swalWithBootstrapButtons.fire(
                'Uscita',
                'Il tuo Prodotto è salvo',
                'error'
              )
            }
          })
    });

    let editMode = false;
    let idModifica = -1;

    function modificaProdotto(prodotto) {
        $.ajax({
            type: "PUT",
            url: "prodotti",
            data: JSON.stringify(prodotto),
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                console.log(response);
                if (response.message === "Crash sistema in modifica"){
                    alert("Problema nella modifica");
                } else if (response.message === "Detto, Fatto") {
                    editMode = false;
                    idModifica = -1;
                    
                }
            }
        });
    }

    $('#output').on('click', '.btn-modifica', function () {
        editMode = true;
        const id = +$(this).attr('data-id');
        idModifica = id;

        $.get(`prodotti/${id}`, function(modifica) {

            $('#nomeProdotto').val(modifica.nome);
            $('#selectCategorie').val(modifica.categoria.id);
            $('#descrizione').val(modifica.descrizione);
            $('#disponibilita').val(modifica.disponibilita);
            $('#prezzo').val(modifica.prezzo);
            $('#btnModificaConf').text('Modifica ' + modifica.nome);
            $('#title').text('Modifica ' + modifica.nome);
         
        });
    });
     
    function getCategorie() {
        $.get('categorie', function (resume) {
            const outputCategoria = $('#outputCategorie');
            for (let i = 0; i < resume.length; i++) {
                $(`<tr id='riga-${resume[i].id}'>
                <td>${resume[i].id}</td>
                <td class='nome'>${resume[i].nome}</td>
                <td class='iva'>${resume[i].iva}</td>
                <td>
                    <div class="btn-group" role="group">
                        <button id="btnGroupDrop1" type="button" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">Opzioni</button>
                            <ul class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                            <li><a class="dropdown-item btn-modifica" data-bs-toggle="modal" data-bs-target="#modificaCat" data-id='${resume[i].id}'>Modifica</a></li>
                                <li><a class="dropdown-item btn-elimina" data-id='${resume[i].id}'>Elimina</a></li>
                            </ul>
                    </div>
                </td>
            </tr>`).hide().appendTo(outputCategoria).fadeIn(i * 150);
            }
          })
    }
    getCategorie();

    function addcategoria(categoria){
        console.log(categoria);
        $.ajax({
            type: 'POST',
            url: '/categorie',
            data: JSON.stringify(categoria),
            contentType: 'application/json',
            dataType: 'json',
            success: function(response){
                if (response.messaggio === "Metodo Post non Funzionante"){
                    alert("Problema nell'aggiunta");
                } else if (response.messaggio === "Detto, Fatto") {
                    window.location.href='categorie.html';
                }
            }
        })
    }

    $('#aggiungiCategoria').click(function () {
        const categoria = {
            nome: $('#nomeCategoria').val(),
            iva: $('#iva').val(),
        }
            addcategoria(categoria);

        $('#nomeCategoria').val('');
        $('#iva').val('');
        
    })

    function deleteCategoria(id) {
        let idPagina = $(`#riga-${id}`);
        
        $.ajax({
            type: "DELETE",
            url: `categorie/${id}`,
            success: function (response) {
                console.log(response);
                if (response.messaggio === "Detto, Fatto") {
                    idPagina.slideUp(300, function () {
                        idPagina.remove();
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Something went wrong!',
                      })
                }
                
            }
        });
    }

    $('#outputCategorie').on('click', '.btn-elimina', function() {
        const id = $(this).attr('data-id');
        Swal.fire({
            title: 'Sei sicuro?',
            text: "Operazione Irreversibile! Cancellando la categoria elimini anche tutti i prodotti collegati",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Elimina',
            cancelButtonText: 'Chiudi'
          }).then((result) => {
            if (result.isConfirmed) {
              Swal.fire(
                'Cancellato!',
                'La tua categoria con rispettivo prodotto è stato Cancellato.',
                'success'
                )
                deleteCategoria(id);
            }
          })
    });

    let editModeCat = false;
    let idModificaCat = -1;

    function modificaCategoria(categoria) {
        $.ajax({
            type: "PUT",
            url: "categorie",
            data: JSON.stringify(categoria),
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                console.log(response);
                if (response.message === "Metodo Put non Funzionante"){
                    alert("Problema nella modifica");
                } else if (response.message === "Detto, Fatto") {
                    editModeCat = false;
                    idModificaCat = -1;
                }
            }
        });
    }

    $('#outputCategorie').on('click', '.btn-modifica', function () {
        editMode = true;
        const id = +$(this).attr('data-id');
        idModificaCat = id;

        $.get(`categorie/${id}`, function(modifica) {

            $('#nomeCategoria').val(modifica.nome);
            $('#iva').val(modifica.iva);
            $('#btnModificaCategoria').text('Modifica ' + modifica.nome);
            $('#titleCategoria').text('Modifica ' + modifica.nome);
         
        });
    });

    $('#btnModificaCategoria').click(function () {
        const categoria = {
            nome: $('#nomeCategoria').val(),
            iva: $('#iva').val(),
        }
        if (editMode) {
            Swal.fire({
                icon: 'question',
                title: 'Vuoi salvare la Modifica di ' + categoria.nome + '?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: `Salva`,
                denyButtonText: `Non Salvare`,
              }).then((result) => {
                /* Read more about isConfirmed, isDenied below */
                if (result.isConfirmed) {
                  Swal.fire('Salvato!', '', 'success')
                  categoria.id = idModificaCat;
                  modificaCategoria(categoria);
                  setTimeout(function () {
                      window.location.href='categorie.html';
                  }, 2000);
                  
                } else if (result.isDenied) {
                  Swal.fire('Modifiche non salvate', '', 'info')
                }
              })
        }
    })

    $ ( "#myInput" ).on ( "keyup", function () {
        var value = $ ( this ).val ().toLowerCase ();
        $ ( "#output tr" ).filter ( function () {
            $ ( this ).toggle ( $ ( this ).text ().toLowerCase ().indexOf ( value ) > -1 )
        } );
    } );
});