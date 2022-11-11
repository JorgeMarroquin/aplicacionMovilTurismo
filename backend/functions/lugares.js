const { PrismaClient } = require("@prisma/client")
const prisma = new PrismaClient()
const {getUserFavoritesId} = require("./favorites")

const getAllLugares = async(userid, tipo) =>{
    let userfavs = await getUserFavoritesId(Number(userid))

    const lugares = await prisma.lugares.findMany({
        where: {
            tipoLugar: tipo
        },
        include:{
            visitas: {
                where: {
                    usuarioId: Number(userid)
                },
                select:{
                    visitaFecha: true
                }
            }
        }
    })
    let result = lugares.map(l => {
        if(l["visitas"].length > 0){
            l["visitaFecha"] = l["visitas"][0].visitaFecha
        }else{
            l["visitaFecha"] = null
        }
        delete l["visitas"]
        
        if(userfavs.includes(l.id)){
            l["isFavorite"] = true
        }else{
            l["isFavorite"] = false
        }
        return l
    })

    return result
}

module.exports = {getAllLugares}