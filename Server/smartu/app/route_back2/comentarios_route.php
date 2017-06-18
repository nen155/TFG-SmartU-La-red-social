<?php
use App\Model\ComentarioModel;

$app->group('/comment/', function () {
    
    $this->get('test', function ($req, $res, $args) {
        return $res->getBody()
                   ->write('Hello Users');
    });
    
    $this->post('comentarios', function ($req, $res) {
        $um = new ComentarioModel();
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
    
    $this->post('comentariosproyecto', function ($req, $res) {
        $um = new ComentarioModel();
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
	
    $this->post('guardar', function ($req, $res) {
        $um = new ComentarioModel();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertComentario(
                    $req->getParsedBody()
                )
            )
        );
    });
	
	

});