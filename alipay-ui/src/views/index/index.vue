<script lang="ts" setup>
import {onMounted} from "vue";
import {useRouter} from "vue-router";

onMounted(() => {
  const router = useRouter();
  if (router.currentRoute.value.path === '/index') {
    router.push('/goods')
  }

})

</script>

<template>
  <div class="index-container">
    <nav>
      <router-link to="/goods">商品</router-link>
      |
      <router-link to="/orders">订单</router-link>
    </nav>
    <div style="margin: 10px auto; width: 70%">
      <router-view/>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.index-container {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;

  nav {
    padding: 30px;
  }

  nav a {
    font-weight: bold;
    color: #2c3e50;
  }

  nav a.router-link-exact-active {
    color: #42b983;
  }
}
</style>