<?php
use App\Model\NotificacionModel;

$app->group('/notification/', function () {
    
    $this->get('test', function ($req, $res, $args) {
        return $res->getBody()
                   ->write('Hello Users');
    });
    
    $this->post('notificaciones', function ($req, $res, $args) {
        $um = new NotificacionModel();
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


});