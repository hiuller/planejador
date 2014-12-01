## Copyright (C) 2014 Usiminas
## 
## This program is free software; you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or
## (at your option) any later version.
## 
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
## 
## You should have received a copy of the GNU General Public License
## along with Octave; see the file COPYING.  If not, see
## <http://www.gnu.org/licenses/>.

## capacidade

## Author: Usiminas <Usiminas@USIMINAS-HP>
## Created: 2014-11-03

  function cap = capacidade(a, p)  
  
      load('params');
      limite_superior = 49; %%% WARNING %%%
      Theta = result(1:6);
      r     = result(7:9);
      
      a_aug = [ones(size(a, 1),1) a];
      
      h = a_aug * Theta';
      
      c = p * r';
      
      cap = (h-c);
      if( cap > limite_superior )
        cap = limite_superior;
      else
        if( cap < 0 )
          cap = 0;
        end
      end
  endfunction
