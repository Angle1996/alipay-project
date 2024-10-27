import request from '../utils/request'

// 获取订单列表
export function list() {
    return request.get('/orders/list')
}

//添加订单
export function add(data: any) {
    return request.post('/orders/add', data)
}
