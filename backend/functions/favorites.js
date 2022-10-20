const { PrismaClient } = require("@prisma/client")
const prisma = new PrismaClient()
const {compareByNombre} = require("./comparations")

const getUserFavoritesId = async(user) =>{
    const userPost = await prisma.usuarios.findUnique({
        where: {
            id: user
        },
        select: {
        
            lugaresFavoritos: {
                select: {
                    lugarId: true
                }
            }
        },
    })

    let userfavs = userPost.lugaresFavoritos.map(l => l.lugarId)

    return userfavs
}

const getSeparatedFavorites = async(usuarioId) => {
    const userFavs = await prisma.usuarios.findUnique({
        where: {
            id: usuarioId
        },
        select: {
        
            lugaresFavoritos: {
                select: {
                    lugar: {
                        include:{
                            visitas: {
                                where: {
                                    usuarioId: Number(usuarioId)
                                },
                                select:{
                                    visitaFecha: true
                                }
                            }
                        }
                    }
                }
            }
        },
    })
    let places = userFavs.lugaresFavoritos.map(i => i.lugar)
    let addinfo = places.map(l => {
        if(l["visitas"].length > 0){
            l["visitaFecha"] = l["visitas"][0].visitaFecha
        }else{
            l["visitaFecha"] = null
        }
        delete l["visitas"]

        l["isFavorite"] = true
        return l
    })

    let tempR = []
    let tempT = []
    addinfo.map(e => {
        if(e.tipoLugar == "RESTAURANTE"){
            tempR.push(e)
        }else{
            tempT.push(e)
        }
    })

    let result = {RESTAURANTE: tempR.sort(compareByNombre), TURISTICO: tempT.sort(compareByNombre)}
    return result
}

module.exports = {getUserFavoritesId, getSeparatedFavorites}
