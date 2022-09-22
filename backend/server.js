const express = require("express")
const { PrismaClient } = require("@prisma/client")

const prisma = new PrismaClient()
const app = express()

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
    const post = await prisma.usuarios.create({
        data: {nombre, apellido, correo, nacionalidad, telefono, password}
    })
    res.json(post)
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
        console.log("no")
        res.status(404).json({msg: "no"})
    }
})

//LUGARES
app.get("/getLugares/:tipo", async(req, res) => {
    let params = req.params
    const result = await prisma.lugares.findMany({
        where: {
            tipoLugar: params.tipo
        }
    })
    res.json(result)
})

//change

app.listen(3000, () => {
    console.log("Server ready at http://localhost:3000")
})