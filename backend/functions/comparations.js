const compareByNombre = ( a, b ) => {
    if ( a.nombre < b.nombre ){
      return -1;
    }
    if ( a.nombre > b.nombre ){
      return 1;
    }
    return 0;
  }

const compareByDistancia = ( a, b ) => {
    if ( Number(a.distancia )< Number(b.distancia) ){
      return -1;
    }
    if ( Number(a.distancia) > Number(b.distancia) ){
      return 1;
    }
    return 0;
  }

module.exports = {compareByNombre, compareByDistancia}