<template>
  <div>
    <input type="file" @change="onFileSelected" />
<button @click="uploadFile">Upload Image</button>
</div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { uploadImage } from '../services/casefileService';

export default defineComponent({
  name: 'UploadImage',
  setup() {
    const selectedFile = ref<File | null>(null);

    const onFileSelected = (event: Event) => {
      const target = event.target as HTMLInputElement;
      if (target.files && target.files[0]) {
        selectedFile.value = target.files[0];
      }
    };

    const uploadFile = async () => {
      if (!selectedFile.value) {
        alert('Please select a file first.');
        return;
      }

      try {
        const caseId = 1;  // 这里可以根据实际情况动态设置 caseId
        const response = await uploadImage(caseId, selectedFile.value);
        console.log('File uploaded successfully', response);
      } catch (error) {
        console.error('Error uploading file', error);
      }
    };

    return {
      onFileSelected,
      uploadFile,
    };
  },
});
</script>
