import request from '../utils/request'

export function pay(orderNo: any) {
    return request.get<any>('/alipay/pay?orderNo=' + orderNo)
}

export function refund(data: any) {
    return request.post<any>('/alipay/refund', data)
}
