(function () {
    'use strict';

    angular.module('sicobaApp')
        .controller('ClienteCtrl', function ($scope, $rootScope, $routeParams, Cliente, Cep, Contrato) {

            $scope.save = _save;
            $scope.buscarEnderecoPorCep = _buscarEnderecoPorCep;

            _init();

            function _init() {
                $scope.cliente = {status: 'ATIVO'};
                $scope.hoje = new Date();

                if ($routeParams.id) {
                    Cliente.get({id: $routeParams.id}, function (cliente) {
                        if(cliente.dataNascimento) cliente.dataNascimento = moment(cliente.dataNascimento).toDate();
                        if(cliente.bypassAutoBlockUntil) cliente.bypassAutoBlockUntil = moment(cliente.bypassAutoBlockUntil).toDate();
                        $scope.cliente = cliente;
                    });
                    $scope.contrato = Contrato.buscarPorCliente({clienteId: $routeParams.id});
                }

            }

            function _save(cliente) {
                Cliente.save(cliente, function (data) {
                    $scope.cliente = data;
                    $rootScope.messages = [{
                        title: 'Sucesso:',
                        body: 'Cliente ' + data.nome + ' foi salvo.',
                        type: 'alert-success'
                    }];
                });
            }

            function _buscarEnderecoPorCep(cep, form) {
                if (cep) {
                    Cep.get({cep: cep}, function (data) {
                        if (data.erro) {
                            form.cep.$error.notFound = true;
                        } else {
                            form.cep.$error.notFound = false;
                            $scope.cliente.endereco.cep = data.cep;
                            $scope.cliente.endereco.logradouro = data.logradouro;

                            $scope.cliente.endereco.bairro = {
                                "nome": data.bairro,
                                "cidade": {
                                    "nome": data.localidade,
                                    "estado": {"uf": data.uf}
                                }
                            };
                        }
                    });
                }
            }
        });
}());
