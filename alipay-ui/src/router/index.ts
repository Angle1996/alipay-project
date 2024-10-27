import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/index'
        },
        {
            path: '/index',
            name: 'index',
            component: () => import('@/views/index/index.vue'),
            children: [
                {
                    path: '/goods',
                    name: 'goods',
                    component: () => import('@/views/goods/index.vue'),
                },
                {
                    path: '/orders',
                    name: 'orders',
                    component: () => import('@/views/orders/index.vue')
                }
            ]
        }

    ]
})

export default router
