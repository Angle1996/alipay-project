import request from '../utils/request'
export function list(){
    return request.get('/goods/list')
}