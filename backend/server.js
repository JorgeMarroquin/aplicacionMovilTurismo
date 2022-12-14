const express = require("express")
const { PrismaClient } = require("@prisma/client")

const prisma = new PrismaClient()
const app = express()

const {compareByNombre, compareByDistancia} = require("./functions/comparations")
const {getUserFavoritesId, getSeparatedFavorites} = require("./functions/favorites")
const {getAllLugares} = require("./functions/lugares")
const {getKilometros} = require("./functions/tools")

app.use(express.json())

app.get('/', (req, res) =>{
    res.send("HOLA")
})

app.post('/testpost', async(req, res)=>{
    console.log(req.body)
    const {title, content} = req.body
    const result = await prisma.test.create({
        data: {
            title, content
        }
    })

    res.json(result)
})

app.get('/testget', async(req, res)=>{
    const results = await prisma.test.findMany()
    res.json(results)
})

app.put(`/testput/:id`, async(req, res)=>{
    const {id} = req.params
    const {title, content} = req.body
    const post = await prisma.test.update({
        where: {id: Number(id)},
        data: {title, content}
    })
    res.json(post)
})

app.delete(`/testdelete/:id`, async(req, res)=>{
    const {id} = req.params
    const post = await prisma.test.delete({
        where: {id: Number(id)}
    })
    res.json(post)
})

//USUARIOS
app.post('/createUser', async(req, res) =>{
    console.log(req.body)
    let {nombre, apellido, correo, nacionalidad, telefono, password} = req.body
    try{
        const post = await prisma.usuarios.create({
            data: {nombre, apellido, correo, nacionalidad, telefono, password}
        })
        res.json(post)
    }catch(e) {
        console.log(e)
        res.status(409)
        res.json(null)
    }
    
    
})

app.get("/login/:email/:password", async(req, res) => {
    let params = req.params
    const result = await prisma.usuarios.findMany({
        where: {
            AND: [
                {
                    correo: params.email
                },
                {
                    password: params.password
                }
            ]
        }
    })
    if(result.length == 1) {

        res.json(result[0])
    }else{
        res.status(404).json({msg: "no"})
    }
})

app.get("/getUserById/:id", async(req, res) => {
    let params = req.params
    const result = await prisma.usuarios.findUnique({
        where: {
            id: parseInt(params.id)
        }
    })
    res.json(result)
})

app.put(`/updateUser`, async(req, res)=>{
    
    try{
        const {id} = req.body
        const {nombre, apellido, correo, nacionalidad, telefono} = req.body
        const post = await prisma.usuarios.update({
            where: {id: Number(id)},
            data: {nombre, apellido, correo, nacionalidad, telefono}
        })
        res.json(post)

    }catch{
        console.log("catch")
        res.status(409)
        res.send(null)
    }
    
})

app.put(`/changePassword/:userId/:password`, async(req, res)=>{
    let params = req.params
    try{
        const post = await prisma.usuarios.update({
            where: {id: Number(params.userId)},
            data: {password: params.password}
        })
        res.json({msg: "Contrase??a actualizada"})
    }catch{
        console.log("Error al actualizar contrase??a")
        res.status(409)
        res.json(null)
    }
       
})

//LUGARES
app.get("/getLugares/:tipo/:user", async(req, res) => {
    let params = req.params
    let result = await getAllLugares(params.user, params.tipo)
    result.sort(compareByNombre)
    res.json(result)
})

app.get("/getLugaresDistancia/:type/:userid/:latitud/:longitud", async(req, res) => {
    let params = req.params
    console.log(Number(params.latitud), Number(params.longitud))
    let lugares = await getAllLugares(params.userid, params.type)
    
    let result = lugares.map(l => {
        l.distancia = getKilometros(Number(params.latitud), Number(params.longitud), Number(l.latitud), Number(l.longitud))
        return l
    })
    result.sort(compareByDistancia)
    res.json(result)
})

app.get("/getLugarById/:id/:user", async(req, res) => {
    let params = req.params
    const result = await prisma.lugares.findUnique({
        where: {
            id: parseInt(params.id)
        },
        include:{
            visitas: {
                where: {
                    usuarioId: Number(params.user)
                },
                select:{
                    visitaFecha: true
                }
            }
        }
    })
    const user = await prisma.LugaresFavoritos.findUnique({
        where: {
            lugarId_usuarioId:
                {
                    lugarId: parseInt(params.id),
                    usuarioId: parseInt(params.user)
                }
        }
    })

    if(user != null){
        result['isFavorite'] = true
    }else{
        result['isFavorite'] = false
    }

    if(result["visitas"].length > 0){
        result["visitaFecha"] = result["visitas"][0].visitaFecha
    }else{
        result["visitaFecha"] = null
    }
    delete result["visitas"]
    res.json(result)
})

//FAVORITOS
app.get("/getUserFavorites/:type/:userid", async(req, res) =>{
    let params = req.params
    let userFavs = await getSeparatedFavorites(Number(params.userid))
    res.json(userFavs[params.type])
})

app.post("/saveFav/:lugar/:user", async(req, res) => {
    console.log("save", req.params)
    let {user, lugar} = req.params
    try{
        const post = await prisma.LugaresFavoritos.create({
            data: {
                usuario: {connect: {id: parseInt(user)}},
                lugar: {connect: {id: parseInt(lugar)}},
            },
        })
        res.json({result: true})
    }catch{
        res.status(409)
        res.send(null)
    }
    
})

app.delete("/deleteFav/:lugar/:user", async(req, res) => {
    let params = req.params
    try{
        const post = await prisma.LugaresFavoritos.delete({
            where: {
                lugarId_usuarioId:
                    {
                        lugarId: parseInt(params.lugar),
                        usuarioId: parseInt(params.user)
                    }
            }
        })
        console.log("Delete fav ", post)
        res.json({result: false})
    }catch{
        res.status(409)
        res.send(null)
    }
    
})

//COMENTARIOS
app.post('/createComment/', async(req, res)=>{
    let {lugarId, usuarioId, fecha, comentario} = req.body
    fecha = new Date(fecha)
    const result = await prisma.comentarios.create({
        data: {
            lugarId, usuarioId, fecha, comentario
        }, 
        include: {
            usuario:{
                select:{
                    nombre: true,
                    apellido: true
                } 
            }
        }
    })
    result["isUser"] = true
    console.log("Create comment")
    res.json(result)
})

app.get('/getCommentsById/:lugarId/:usuarioId', async(req, res)=>{
    const params = req.params
    const usercomments = await prisma.usuarios.findUnique({
        where: {
            id: parseInt(params.usuarioId)
        },
        select: {
        
            comentarios: {
                select: {
                    id: true
                }
            },
        },
    })
    let userC = usercomments.comentarios.map(l => l.id)

    const comments = await prisma.comentarios.findMany({
        where: {
            lugarId: parseInt(params.lugarId)
        },
        include: {
            usuario:{
                select:{
                    nombre: true,
                    apellido: true
                } 
            }
        }
    })

    let result = comments.map(c => {
        if(userC.includes(c.id)){
            c["isUser"] = true
        }else{
            c["isUser"] = false
        }
        return c
    })
    res.json(result)
})

app.delete(`/deleteComment/:id`, async(req, res)=>{
    const {id} = req.params
    const del = await prisma.comentarios.delete({
        where: {id: Number(id)}
    })
    console.log("Comentario ", id, " eliminado")
    res.json(del)
})

//SCORES
app.get("/getScoreInPlace/:userId/:lugarId", async(req, res) =>  {
    const params = req.params
    const results = await prisma.scores.findUnique({
        where: {
            lugarId_usuarioId: {
                lugarId: Number(params.lugarId),
                usuarioId: Number(params.userId)
            }
        }
    })
    res.json(results)

})

app.post("/changeScore", async(req, res) => {
    const params = req.body
    const validate = await prisma.scores.findUnique({
        where: {
            lugarId_usuarioId: {
                lugarId: Number(params.lugarId),
                usuarioId: Number(params.usuarioId)
            }
        }
    })
    if(validate == null){
        const post = await prisma.scores.create({
            data: {
                usuario: {connect: {id: parseInt(params.usuarioId)}},
                lugar: {connect: {id: parseInt(params.lugarId)}},
                score: parseFloat(params.score)
            },
        })
        console.log("Score a??adido")
        res.json(post)
    }else{
        const put = await prisma.scores.update({
            where: {
                lugarId_usuarioId:{
                    lugarId: Number(params.lugarId),
                    usuarioId: Number(params.usuarioId)
                }
            },
            data: {
                usuario: {connect: {id: parseInt(params.usuarioId)}},
                lugar: {connect: {id: parseInt(params.lugarId)}},
                score: parseFloat(params.score)
            }
        })

        console.log("Score modificado")
        res.json(put)
    }
})


app.post("/changevisitaDate", async(req, res) => {
    const params = req.body
    const validate = await prisma.visitas.findUnique({
        where: {
            lugarId_usuarioId: {
                lugarId: Number(params.lugarId),
                usuarioId: Number(params.usuarioId)
            }
        }
    })
    if(validate == null){
        const post = await prisma.visitas.create({
            data: {
                usuario: {connect: {id: parseInt(params.usuarioId)}},
                lugar: {connect: {id: parseInt(params.lugarId)}},
                visitaFecha: new Date(params.visitaFecha)
            },
        })
        console.log("Visita a??adida")
        res.json(post)
    }else{
        const put = await prisma.visitas.update({
            where: {
                lugarId_usuarioId:{
                    lugarId: Number(params.lugarId),
                    usuarioId: Number(params.usuarioId)
                }
            },
            data: {
                usuario: {connect: {id: parseInt(params.usuarioId)}},
                lugar: {connect: {id: parseInt(params.lugarId)}},
                visitaFecha: new Date(params.visitaFecha)
            }
        })

        console.log("Visita modificado")
        res.json(put)
    }
})


app.listen(3000, () => {
    console.log("Server ready at http://localhost:3000")
})