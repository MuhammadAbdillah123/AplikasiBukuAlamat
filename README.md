# AplikasiBukuAlamat

Aplikasi Buku Alamat adalah aplikasi berbasis Java yang dirancang untuk mengelola data kontak pengguna. Aplikasi ini memungkinkan pengguna untuk menambahkan, mengedit, menghapus, dan menyimpan data kontak dalam file CSV. Data disimpan secara permanen dalam database SQLite untuk memudahkan pengelolaan kontak.

## Keunggulan Aplikasi

- *Antarmuka Pengguna Sederhana*: Dibangun menggunakan Java Swing sehingga mudah digunakan.
- *Penyimpanan Permanen*: Data kontak disimpan di SQLite untuk menghindari kehilangan data saat aplikasi ditutup.
- *Ekspor Data ke CSV*: Mendukung ekspor data kontak untuk keperluan berbagi atau penyimpanan eksternal.
- *Validasi Input*: Input pengguna divalidasi untuk memastikan semua data yang dimasukkan valid.

## Pembuat Aplikasi

 Tugas UTS - Muhammad Abdillah (2210010152)

## Fitur Utama

1. *Tambah Kontak*  
   Pengguna dapat menambahkan kontak baru dengan memasukkan nama, alamat, nomor telepon, dan email.

2. *Edit Kontak*  
   Pengguna dapat mengedit salah satu kolom data kontak (Nama, Alamat, Telepon, atau Email).

3. *Hapus Kontak*  
   Pengguna dapat menghapus kontak dari daftar jika tidak lagi diperlukan.

4. *Simpan ke CSV*  
   Data kontak dapat diekspor ke file CSV untuk penyimpanan atau berbagi data.

5. *Database Lokal*  
   Semua data kontak disimpan di database SQLite untuk memastikan data tetap aman.

## Cara Menjalankan

1. Clone repositori ini atau unduh file ZIP-nya.
2. Buka proyek di IDE seperti NetBeans atau IntelliJ IDEA.
3. Pastikan JDK dan SQLite telah terinstal di komputer Anda.
4. Jalankan file TugasUts.java untuk memulai aplikasi.

## Panduan Penggunaan

### Menambahkan Kontak
1. Isi semua field input: Nama, Alamat, Telepon, dan Email.
2. Klik tombol *Tambah* untuk menyimpan kontak ke database.

### Mengedit Kontak
1. Pilih baris kontak dari tabel.
2. Klik tombol *Edit*, lalu pilih kolom yang ingin diedit.
3. Masukkan nilai baru dan klik *OK* untuk menyimpan perubahan.

### Menghapus Kontak
1. Pilih baris kontak yang ingin dihapus dari tabel.
2. Klik tombol *Hapus* untuk menghapus kontak dari database.

### Menyimpan Data ke CSV
1. Klik tombol *Simpan*.
2. Pilih lokasi file dan klik *OK* untuk menyimpan data ke file CSV.

### Keluar Aplikasi
Klik tombol *X* pada jendela aplikasi untuk menutup program.

## Teknologi yang Digunakan

- *Java Swing*: Untuk membangun antarmuka pengguna.
- *SQLite*: Untuk penyimpanan data secara lokal.
- *JDBC*: Untuk menghubungkan Java dengan database SQLite.

## Demo

![Screen Recording 2024-11-24 184230](https://github.com/user-attachments/assets/7d8a68dd-eed3-4590-9957-9098ec4033e5)
