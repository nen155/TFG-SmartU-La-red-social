<?php
use App\Model\UserModel;

$app->group('/user/', function () {
    
    $this->get('test', function ($req, $res, $args) {
        return $res->getBody()
                   ->write('Hello Users');
    });
    
    $this->post('usuarios', function ($req, $res, $args) {
        $um = new UserModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->GetAll($data["offset"],$data["limit"])
            )
        );
    });
    
    $this->post('integrantes', function ($req, $res, $args) {
        $um = new UserModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->GetAll($data["offset"],$data["limit"],$data["idProyecto"])
            )
        );
    });
	
	$this->post('login', function ($req, $res, $args) {
        $um = new UserModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->login($data["email"],$data["password"])
            )
        );
    });
    
    $this->post('guardausuario', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertOrUpdate(
                    $req->getParsedBody()
                )
            )
        );
    });
	
	$this->post('seguir', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertSeguidor(
                    $req->getParsedBody()
                )
            )
        );
    });
	
	$this->post('dejarseguir', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->DeleteSeguidor(
                    $req->getParsedBody()
                )
            )
        );
    });
    
	$this->post('buenaidea', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertBuenaIdea(
                    $req->getParsedBody()
                )
            )
        );
    });
	
	$this->post('noesbuenaidea', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->DeleteBuenaIdea(
                    $req->getParsedBody()
                )
            )
        );
    });
	
	$this->post('solicitudunion', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertSolicitudUnion(
                    $req->getParsedBody()
                )
            )
        );
    });
	
	$this->post('eliminasolicitudunion', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->DeleteSolicitudUnion(
                    $req->getParsedBody()
                )
            )
        );
    });
	
	$this->post('interes', function ($req, $res) {
        $um = new UserModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertAreasInteres(
                    $req->getParsedBody()
                )
            )
        );
    });

});